package com.jetbrains.teamsys.dnq.database;

import jetbrains.exodus.database.TransientEntityStore;
import jetbrains.exodus.entitystore.*;
import jetbrains.exodus.entitystore.iterate.EntityIterableBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Wrapper for persistent iterable. Handles iterator.next and delegates it to transient session.
 *
 * @author Vadim.Gurov
 */
public class PersistentEntityIterableWrapper extends EntityIterableBase implements EntityIterableWrapper {

    final EntityIterableBase wrappedIterable;
    protected final TransientEntityStore store;

    PersistentEntityIterableWrapper(@NotNull final TransientEntityStore store, @NotNull EntityIterable wrappedIterable) {
        super(getWrappedTransaction((EntityIterableBase) wrappedIterable));
        if (wrappedIterable instanceof PersistentEntityIterableWrapper) {
            throw new IllegalArgumentException("Can't wrap transient entity iterable with another transient entity iterable.");
        }
        this.store = store;
        this.wrappedIterable = ((EntityIterableBase) wrappedIterable).getSource();
    }

    @Nullable
    private static PersistentStoreTransaction getWrappedTransaction(@NotNull EntityIterableBase wrappedIterable) {
        final EntityIterableBase source = wrappedIterable.getSource();
        return source == EntityIterableBase.EMPTY ? null : source.getTransaction();
    }

    public long size() {
        return wrappedIterable.size();
    }

    public long count() {
        return wrappedIterable.count();
    }

    public long getRoughCount() {
        return wrappedIterable.getRoughCount();
    }

    public long getRoughSize() {
        return wrappedIterable.getRoughSize();
    }

    public int indexOf(@NotNull Entity entity) {
        return wrappedIterable.indexOf(entity);
    }

    public boolean contains(@NotNull Entity entity) {
        return wrappedIterable.contains(entity);
    }

    @NotNull
    public EntityIterableHandle getHandleImpl() {
        return wrappedIterable.getHandle();
    }

    @NotNull
    public EntityIterable intersect(@NotNull EntityIterable right) {
        if (right instanceof EntityIterableBase) {
            return wrappedIterable.intersect(((EntityIterableBase) right).getSource());
        }
        return throwUnsupported();
    }

    @NotNull
    public EntityIterable intersectSavingOrder(@NotNull EntityIterable right) {
        if (right instanceof EntityIterableBase) {
            return wrappedIterable.intersectSavingOrder(((EntityIterableBase) right).getSource());
        }
        return throwUnsupported();
    }

    @NotNull
    public EntityIterable union(@NotNull EntityIterable right) {
        if (right instanceof EntityIterableBase) {
            return wrappedIterable.union(((EntityIterableBase) right).getSource());
        }
        return throwUnsupported();
    }

    @NotNull
    public EntityIterable minus(@NotNull EntityIterable right) {
        if (right instanceof EntityIterableBase) {
            return wrappedIterable.minus(((EntityIterableBase) right).getSource());
        }
        return throwUnsupported();
    }

    @NotNull
    public EntityIterable concat(@NotNull EntityIterable right) {
        if (right instanceof EntityIterableBase) {
            return wrappedIterable.concat(((EntityIterableBase) right).getSource());
        }
        return throwUnsupported();
    }

    @NotNull
    @Override
    public EntityIterable take(int number) {
        return wrappedIterable.take(number);
    }

    @Override
    public EntityIterable findLinks(@NotNull EntityIterable entities, @NotNull String linkName) {
        return wrappedIterable.findLinks(entities, linkName);
    }

    @NotNull
    @Override
    public EntityIterable distinct() {
        return wrappedIterable.distinct();
    }

    @NotNull
    @Override
    public EntityIterable selectDistinct(@NotNull String linkName) {
        return wrappedIterable.selectDistinct(linkName);
    }

    @NotNull
    @Override
    public EntityIterable selectManyDistinct(@NotNull String linkName) {
        return wrappedIterable.selectManyDistinct(linkName);
    }

    @Nullable
    @Override
    public Entity getFirst() {
        return wrap(store, wrappedIterable.getFirst());
    }

    @Nullable
    @Override
    public Entity getLast() {
        return wrap(store, wrappedIterable.getLast());
    }

    @NotNull
    @Override
    public EntityIterable reverse() {
        return wrappedIterable.reverse();
    }

    public boolean isSortResult() {
        return wrappedIterable.isSortResult();
    }

    @NotNull
    public EntityIterable asSortResult() {
        return new PersistentEntityIterableWrapper(store, wrappedIterable.asSortResult());
    }

    @NotNull
    public EntityIterableBase getSource() {
        return wrappedIterable;
    }

    public EntityIterator iterator() {
        return new PersistentEntityIteratorWrapper(wrappedIterable.iterator(), store.getThreadSession());
    }

    @NotNull
    @Override
    public EntityIterator getIteratorImpl(@NotNull PersistentStoreTransaction txn) {
        throw new UnsupportedOperationException("Should never be called");
    }

    public boolean isEmpty() {
        return wrappedIterable.isEmpty();
    }

    private static EntityIterable throwUnsupported() {
        throw new UnsupportedOperationException("Should never be called");
    }

    private static Entity wrap(TransientEntityStore store, Entity entity) {
        return entity == null ? null : store.getThreadSession().newEntity(entity);
    }
}
