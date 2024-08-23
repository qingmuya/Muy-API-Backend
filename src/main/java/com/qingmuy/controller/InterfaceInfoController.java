package com.qingmuy.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingmuy.annotation.AuthCheck;
import com.qingmuy.common.BaseResponse;
import com.qingmuy.common.DeleteRequest;
import com.qingmuy.common.ErrorCode;
import com.qingmuy.common.ResultUtils;
import com.qingmuy.exception.BusinessException;
import com.qingmuy.exception.ThrowUtils;
import com.qingmuy.mapper.InterfaceInfoMapper;
import com.qingmuy.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.qingmuy.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.qingmuy.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.qingmuy.model.entity.InterfaceInfo;
import com.qingmuy.model.entity.User;
import com.qingmuy.service.InterfaceInfoService;
import com.qingmuy.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
@Slf4j
@RestController
@RequestMapping("/interfaceInfo")
public class InterfaceInfoController {

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    InterfaceInfoMapper interfaceInfoMapper;

    /**
     * 创建接口
     * @param interfaceInfoAddRequest 接口信息
     * @param request 请求头
     * @return 是否添加成功
     */
    @ApiOperation(value = "添加")
    @PostMapping("/add")
    @AuthCheck
    public BaseResponse<String> addInterface(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtil.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setCreateUser(loginUser.getId());
        interfaceInfoService.save(interfaceInfo);
        return ResultUtils.success("添加成功");
    }

    @ApiOperation(value = "根据id查询")
    @GetMapping("/get/{id}")
    @AuthCheck
    public BaseResponse<InterfaceInfo> getInterfaceById(@PathVariable("id") Long id) {
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该接口不存在");
        }
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 更新接口
     *
     * @param interfaceInfoUpdateRequest 更新接口数据
     * @param request 请求头
     * @return 修改结果
     */
    @ApiOperation(value = "更新")
    @PostMapping("/update")
    @AuthCheck
    public BaseResponse<String> updateInterface(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                                HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceInfoUpdateRequest.getId());
        BeanUtil.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 仅本人或管理员可修改
        User user = userService.getLoginUser(request);
        if (!interfaceInfo.getCreateUser().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success("修改成功");
    }

    @ApiOperation(value = "查询")
    @PostMapping("/list")
    public BaseResponse<Page<InterfaceInfo>> listInterface(@RequestBody InterfaceInfoQueryRequest queryRequest) {
        // 逻辑处理
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(queryRequest));
        return ResultUtils.success(interfaceInfoPage);
    }

    /**
     * 删除接口
     * @param deleteRequest 接口信息
     * @return 是否删除成功
     */
    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    @AuthCheck
    public BaseResponse<String> deleteInterface(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(deleteRequest.getId());
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        interfaceInfoService.removeById(deleteRequest.getId());
        return ResultUtils.success("删除成功");
    }

}