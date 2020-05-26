package com.c2olshare.registry.common.base;

import com.c2olshare.registry.common.constant.ErrorCode;
import com.c2olshare.registry.common.dto.HttpResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author MaJing
 */
public abstract class BaseController<T extends BaseEntity, ID extends String> {

    /**
     * 注入service
     *
     * @return BaseService
     */
    protected abstract BaseService<T, ID> baseService();

    @GetMapping("/list")
    public List<T> list(@RequestParam(name = "sort", defaultValue = "createTime", required = false) String[] sort,
                        @RequestParam(name = "direction", defaultValue = "ASC", required = false) Sort.Direction direction,
                        @RequestParam(name = "matcher", defaultValue = "CONTAINING", required = false) String matcher,
                        T query) {
        return baseService().findAll(query, Sort.by(direction, sort), matcher);
    }

    @GetMapping("/page")
    public Page<T> page(@PageableDefault(sort = {"createTime"}, direction = Sort.Direction.ASC) Pageable pageable,
                        @RequestParam(name = "matcher", defaultValue = "CONTAINING", required = false) String matcher,
                        T query) {
        return baseService().findAll(query, pageable, matcher);
    }

    @GetMapping("/{id}")
    public T get(@PathVariable("id") ID id) {
        return baseService().findById(id);
    }

    @PostMapping
    public T save(@RequestBody T t) {
        return baseService().save(t);
    }

    @PutMapping("/{id}")
    public T update(@PathVariable("id") ID id, @RequestBody T t) {
        return baseService().update(id, t);
    }

    @DeleteMapping("/{id}")
    public HttpResponse<String> delete(@PathVariable("id") ID id) {
        baseService().deleteById(id);
        return HttpResponse.build(ErrorCode.SUCCESS, "删除成功");
    }

    @PostMapping("/delete")
    public HttpResponse<String> delete(@RequestBody List<ID> ids) {
        baseService().deleteByIds(ids);
        return HttpResponse.build(ErrorCode.SUCCESS, "删除成功");
    }
}
