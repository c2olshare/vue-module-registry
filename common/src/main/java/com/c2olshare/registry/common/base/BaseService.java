package com.c2olshare.registry.common.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * BaseService
 *
 * @author MaJing
 */
public interface BaseService<T, ID> {

    /**
     * 保存实体对象
     *
     * @param t 对象
     * @return T
     */
    T save(T t);

    /**
     * 根据ID删除数据
     *
     * @param id ID
     */
    void deleteById(ID id);

    /**
     * 批量删除对象
     *
     * @param iterable ids
     */
    void deleteByIds(Iterable<ID> iterable);

    /**
     * 更新实体对象
     *
     * @param id id
     * @param t  实体对象
     * @return T
     */
    T update(ID id, T t);

    /**
     * 查询所有数据
     *
     * @return list
     */
    List<T> findAll();

    /**
     * 查询所有数据
     *
     * @param sort 排序
     * @return list
     */
    List<T> findAll(Sort sort);

    /**
     * 查询所有数据
     *
     * @param query 查询条件
     * @return list
     */
    List<T> findAll(T query);

    /**
     * 查询所有数据
     *
     * @param query 查询条件
     * @param sort  排序
     * @return list
     */
    List<T> findAll(T query, Sort sort);

    /**
     * 分页查询所有数据
     *
     * @param query    查询条件
     * @param pageable 分页
     * @return page
     */
    Page<T> findAll(T query, Pageable pageable);

    /**
     * 查询所有数据
     *
     * @param query   查询条件
     * @param matcher 匹配规则
     * @return list
     */
    List<T> findAll(T query, String matcher);

    /**
     * 查询所有数据
     *
     * @param query   查询条件
     * @param sort    排序
     * @param matcher 匹配规则
     * @return list
     */
    List<T> findAll(T query, Sort sort, String matcher);

    /**
     * 分页查询所有数据
     *
     * @param query    查询条件
     * @param pageable 分页
     * @param matcher  匹配规则
     * @return page
     */
    Page<T> findAll(T query, Pageable pageable, String matcher);

    /**
     * 根据ID查询数据
     *
     * @param id id
     * @return T
     */
    T findById(ID id);

    /**
     * 加锁查询
     *
     * @param id id
     * @return T
     */
    T findWithLock(ID id);
}
