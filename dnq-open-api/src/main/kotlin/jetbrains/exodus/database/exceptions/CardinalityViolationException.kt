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
package jetbrains.exodus.database.exceptions

import jetbrains.exodus.database.TransientEntity
import jetbrains.exodus.query.metadata.AssociationEndMetaData

open class CardinalityViolationException(message: String, entity: TransientEntity, associationEndName: String) :
        DataIntegrityViolationException(message, "Value is required", entity) {

    override val entityFieldHandler = EntityFieldHandler(entity.id, associationEndName)

    constructor(entity: TransientEntity, md: AssociationEndMetaData) :
            this("Cardinality violation for [$entity.${md.name}]. Required cardinality is [${md.cardinality.getName()}]", entity, md.name)

    override fun relatesTo(entity: TransientEntity, fieldIdentity: Any?): Boolean {
        return super.relatesTo(entity, fieldIdentity) && fieldIdentity == entityFieldHandler.fieldName
    }
}
