package com.c2olshare.registry.common.base;


import com.c2olshare.registry.common.util.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

/**
 * @author MaJing
 */
@Transactional(rollbackFor = Exception.class)
public abstract class BaseServiceImpl<T extends BaseEntity, ID extends String> implements BaseService<T, ID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseServiceImpl.class);

    private Class<T> entityClass;

    public BaseServiceImpl() {
        this.entityClass = this.getEntityClass();
    }

    /**
     * 注入repository
     *
     * @return BaseRepository
     */
    protected abstract BaseRepository<T, ID> baseRepository();

    /**
     * 条件查询<br/>
     * 模糊匹配,忽略大小写<br/>
     *
     * @param query 查询条件
     * @return example
     */
    protected Example<T> getExample(T query, String matcher) {
        ExampleMatcher.StringMatcher stringMatcher = ExampleMatcher.StringMatcher.EXACT;
        try {
            if (StringUtils.hasText(matcher)) {
                stringMatcher = ExampleMatcher.StringMatcher.valueOf(matcher.toUpperCase());
            }
        } catch (Exception e) {
            // ignore
        }

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(stringMatcher)
                .withIgnoreNullValues()
                .withIgnoreCase(true)
                // 忽略乐观锁
                .withIgnorePaths("lock_version");
        return Example.of(query, exampleMatcher);
    }

    @Override
    public T save(T t) {
        return baseRepository().save(t);
    }

    @Override
    public void deleteById(ID id) {
        baseRepository().deleteById(id);
    }

    @Override
    public void deleteByIds(Iterable<ID> ids) {
        for (ID id : ids) {
            this.deleteById(id);
        }
    }

    @Override
    public T update(ID id, T t) {
        Assert.notNull(id, "Id must not be null");
        Assert.notNull(t.getLockVersion(), "Lock version must not be null");

        Optional<T> optional = baseRepository().findById(id);
        if (optional.isPresent()) {
            try {
                // 解决乐观锁失效问题
                T instance = entityClass.newInstance();
                BeanUtils.copyProperties(optional.get(), instance);
                BeanUtils.copyPropertiesIgnoreNull(t, instance);
                return baseRepository().save(instance);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            LOGGER.error("{} entity not exist with id: {}", entityClass.getSimpleName(), id);
            return null;
        }
    }

    @Override
    public List<T> findAll() {
        return baseRepository().findAll();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return baseRepository().findAll(sort);
    }

    @Override
    public List<T> findAll(T query) {
        return this.findAll(query, "");
    }

    @Override
    public List<T> findAll(T query, Sort sort) {
        return this.findAll(query, sort, "");
    }

    @Override
    public Page<T> findAll(T query, Pageable pageable) {
        return this.findAll(query, pageable, "");
    }

    @Override
    public List<T> findAll(T query, String matcher) {
        Example<T> example = getExample(query, matcher);
        return baseRepository().findAll(example);
    }

    @Override
    public List<T> findAll(T query, Sort sort, String matcher) {
        Example<T> example = getExample(query, matcher);
        return baseRepository().findAll(example, sort);
    }

    @Override
    public Page<T> findAll(T query, Pageable pageable, String matcher) {
        Example<T> example = getExample(query, matcher);
        return baseRepository().findAll(example, pageable);
    }

    @Override
    public T findById(ID id) {
        Optional<T> optional = baseRepository().findById(id);
        return optional.orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    public T findWithLock(ID id) {
        throw new UnsupportedOperationException("Unimplemented method");
    }

    @SuppressWarnings("unchecked")
    protected Class<T> getEntityClass() {
        Class<?> clazz = this.getClass();
        Type type = clazz.getGenericSuperclass();

        if (type instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType) type).getActualTypeArguments();
            return (Class<T>) p[0];
        }
        return null;
    }
}
