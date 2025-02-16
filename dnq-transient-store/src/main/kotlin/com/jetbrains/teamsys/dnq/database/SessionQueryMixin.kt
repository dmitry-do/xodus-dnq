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
package com.jetbrains.teamsys.dnq.database

import jetbrains.exodus.database.TransientStoreSession
import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.EntityIterable
import jetbrains.exodus.entitystore.PersistentStoreTransaction

internal interface SessionQueryMixin : TransientStoreSession {
    val persistentTransactionInternal: PersistentStoreTransaction

    fun wrap(action: String, entityIterable: EntityIterable): EntityIterable

    override fun getAll(entityType: String): EntityIterable {
        return wrap("getAll", persistentTransactionInternal.getAll(entityType))
    }

    override fun getSingletonIterable(entity: Entity): EntityIterable {
        return wrap("getSingletonIterable", persistentTransactionInternal.getSingletonIterable(
                (entity as TransientEntityImpl).persistentEntity
        ))
    }

    override fun find(entityType: String, propertyName: String, value: Comparable<*>): EntityIterable {
        return wrap("find", persistentTransactionInternal.find(
                entityType,
                propertyName,
                value
        ))
    }

    override fun find(entityType: String, propertyName: String, minValue: Comparable<*>, maxValue: Comparable<*>): EntityIterable {
        return wrap("find", persistentTransactionInternal.find(
                entityType,
                propertyName,
                minValue,
                maxValue
        ))
    }

    override fun findContaining(entityType: String, propertyName: String, value: String, ignoreCase: Boolean): EntityIterable {
        return wrap("findContaining", persistentTransactionInternal.findContaining(
                entityType,
                propertyName,
                value,
                ignoreCase
        ))
    }

    override fun findIds(entityType: String, minValue: Long, maxValue: Long): EntityIterable {
        return wrap("findIds", persistentTransactionInternal.findIds(
                entityType,
                minValue,
                maxValue
        ))
    }

    override fun findWithProp(entityType: String, propertyName: String): EntityIterable {
        return wrap("findWithProp", persistentTransactionInternal.findWithProp(
                entityType,
                propertyName
        ))
    }

    override fun findStartingWith(entityType: String, propertyName: String, value: String): EntityIterable {
        return wrap("startsWith", persistentTransactionInternal.findStartingWith(
                entityType,
                propertyName,
                value
        ))
    }

    override fun findWithBlob(entityType: String, propertyName: String): EntityIterable {
        return wrap("findWithBlob", persistentTransactionInternal.findWithBlob(
                entityType,
                propertyName
        ))
    }

    override fun findLinks(entityType: String, entity: Entity, linkName: String): EntityIterable {
        return wrap("findLinks", persistentTransactionInternal.findLinks(
                entityType,
                entity,
                linkName
        ))
    }

    override fun findLinks(entityType: String, entities: EntityIterable, linkName: String): EntityIterable {
        return wrap("findLinks", persistentTransactionInternal.findLinks(
                entityType,
                entities,
                linkName
        ))
    }

    override fun findWithLinks(entityType: String, linkName: String): EntityIterable {
        return wrap("findWithLinks", persistentTransactionInternal.findWithLinks(
                entityType,
                linkName
        ))
    }

    override fun findWithLinks(entityType: String,
                               linkName: String,
                               oppositeEntityType: String,
                               oppositeLinkName: String): EntityIterable {
        return wrap("findWithLinks", persistentTransactionInternal.findWithLinks(
                entityType,
                linkName,
                oppositeEntityType,
                oppositeLinkName
        ))
    }

    override fun sort(entityType: String,
                      propertyName: String,
                      ascending: Boolean): EntityIterable {
        return wrap("sort", persistentTransactionInternal.sort(
                entityType,
                propertyName,
                ascending
        ))
    }

    override fun sort(entityType: String,
                      propertyName: String,
                      rightOrder: EntityIterable,
                      ascending: Boolean): EntityIterable {
        return wrap("sort", persistentTransactionInternal.sort(
                entityType,
                propertyName,
                rightOrder,
                ascending
        ))
    }

    override fun sortLinks(entityType: String,
                           sortedLinks: EntityIterable,
                           isMultiple: Boolean,
                           linkName: String,
                           rightOrder: EntityIterable): EntityIterable {
        return wrap("sortLinks", persistentTransactionInternal.sortLinks(
                entityType,
                sortedLinks,
                isMultiple,
                linkName,
                rightOrder
        ))
    }

    override fun sortLinks(entityType: String,
                           sortedLinks: EntityIterable,
                           isMultiple: Boolean,
                           linkName: String,
                           rightOrder: EntityIterable,
                           oppositeEntityType: String,
                           oppositeLinkName: String): EntityIterable {
        return wrap("sortLinks", persistentTransactionInternal.sortLinks(
                entityType,
                sortedLinks,
                isMultiple,
                linkName,
                rightOrder,
                oppositeEntityType,
                oppositeLinkName
        ))
    }

    override fun mergeSorted(sorted: List<EntityIterable>, comparator: Comparator<Entity>): EntityIterable {
        return wrap("mergeSorted", persistentTransactionInternal.mergeSorted(
                sorted,
                comparator
        ))
    }
}
