/**
 * Copyright 2006 - 2023 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kotlinx.dnq.link

import jetbrains.exodus.query.metadata.AssociationEndCardinality
import jetbrains.exodus.query.metadata.AssociationEndType
import kotlinx.dnq.RequiredPropertyUndefinedException
import kotlinx.dnq.XdEntity
import kotlinx.dnq.XdEntityType
import kotlinx.dnq.util.reattach
import kotlinx.dnq.util.reattachAndGetLink
import kotlinx.dnq.util.threadSessionOrThrow
import kotlin.reflect.KProperty

class XdToOneRequiredLink<in R : XdEntity, T : XdEntity>(
        oppositeEntityType: XdEntityType<T>,
        dbPropertyName: String?,
        onDeletePolicy: OnDeletePolicy,
        onTargetDeletePolicy: OnDeletePolicy
) : ScalarRequiredLink<R, T>, XdLink<R, T>(
        oppositeEntityType,
        dbPropertyName,
        null,
        AssociationEndCardinality._1,
        AssociationEndType.DirectedAssociationEnd,
        onDeletePolicy,
        onTargetDeletePolicy
) {

    override fun getValue(thisRef: R, property: KProperty<*>): T {
        val entity = thisRef.reattachAndGetLink(property.dbName)
                ?: throw RequiredPropertyUndefinedException(thisRef, property)
        return oppositeEntityType.wrap(entity)
    }

    override fun setValue(thisRef: R, property: KProperty<*>, value: T) {
        val session = thisRef.threadSessionOrThrow
        thisRef.reattach(session).setToOne(property.dbName, value.reattach(session))
    }

    override fun isDefined(thisRef: R, property: KProperty<*>): Boolean {
        return thisRef.reattachAndGetLink(property.dbName) != null
    }
}
