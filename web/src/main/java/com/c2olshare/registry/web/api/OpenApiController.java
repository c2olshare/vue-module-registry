package com.c2olshare.registry.web.api;

import com.c2olshare.registry.common.constant.ErrorCode;
import com.c2olshare.registry.common.dto.HttpResponse;
import com.c2olshare.registry.web.constant.ApiPrefix;
import com.c2olshare.registry.web.constant.Variables;
import com.c2olshare.registry.web.dto.ModuleDTO;
import com.c2olshare.registry.web.service.OpenApiService;
import org.springframework.web.bind.annotation.*;

/**
 * @author MaJing
 */
@RestController
@RequestMapping(ApiPrefix.OPEN)
public class OpenApiController {

    private final OpenApiService openApiService;

    public OpenApiController(OpenApiService openApiService) {
        this.openApiService = openApiService;
    }

    @GetMapping("module/{code}")
    public HttpResponse<?> publish(@PathVariable("code") String code,
                                   @RequestParam(value = "namespace", defaultValue = Variables.DEFAULT_NAMESPACE_CODE) String namespace) {
        ModuleDTO moduleDTO = openApiService.findByCode(code, namespace);
        return HttpResponse.build(ErrorCode.SUCCESS, null, moduleDTO);
    }

}
