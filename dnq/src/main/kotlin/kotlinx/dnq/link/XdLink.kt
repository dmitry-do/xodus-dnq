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
import kotlinx.dnq.XdEntity
import kotlinx.dnq.XdEntityType
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

abstract class XdLink<in R, out T : XdEntity>(
        val oppositeEntityType: XdEntityType<T>,
        val dbPropertyName: String?,
        val dbOppositePropertyName: String?,
        val cardinality: AssociationEndCardinality, val endType: AssociationEndType, val onDelete: OnDeletePolicy, val onTargetDelete: OnDeletePolicy) {

    open val oppositeField: KProperty1<*, *>?
        get() = null

    protected val KProperty<*>.dbName get() = dbPropertyName ?: name
    protected val KProperty<*>.oppositeDbName get() = dbOppositePropertyName ?: name

    abstract fun isDefined(thisRef: R, property: KProperty<*>): Boolean
}
