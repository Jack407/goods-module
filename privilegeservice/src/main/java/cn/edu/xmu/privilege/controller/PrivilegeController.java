package cn.edu.xmu.privilege.controller;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.privilege.model.vo.RoleVo;
import cn.edu.xmu.privilege.service.RoleService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 权限控制器
 * @author Ming Qiu
 **/
@Api(value = "权限服务", tags = "privilege")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class PrivilegeController {

    private final Logger logger = LoggerFactory.getLogger(PrivilegeController.class);

    @Autowired
    RoleService roleService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @ApiOperation(value = "",  produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value ="page", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value ="pageSize", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("roles")
    public Object selectAllRoles(@RequestParam Integer page,@RequestParam Integer pageSize) {
        return ResponseUtil.ok(roleService.selectAllRoles(page,pageSize));
    }

    @ApiOperation(value = "",  produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "RoleVo", name = "vo", value ="vo", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("roles")
    public Object insertRole(@Validated @RequestBody RoleVo vo, BindingResult bindingResult) {
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject){
            return returnObject;
        }
        ReturnObject<VoObject> retObject = roleService.insertRole(vo);
        httpServletResponse.setStatus(HttpStatus.CREATED.value());
        return Common.getRetObject(retObject);
    }

    @ApiOperation(value = "删除商品",  produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "id", value ="商品对象id" ,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @DeleteMapping("roles/{id}")
    public Object deleteRole(@PathVariable("id") Long id) {
        ReturnObject<Object> returnObject = roleService.deleteRole(id);
        return Common.getNullRetObj(returnObject, httpServletResponse);
    }

    @ApiOperation(value = "修改商品",  produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "id", value ="商品对象id" ,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PutMapping("roles/{id}")
    public Object updateRole(@PathVariable Long id, @Validated @RequestBody RoleVo vo, BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject){
            return returnObject;
        }
        ReturnObject<Object> retObject = roleService.updateRole(id, vo);
        return Common.getNullRetObj(retObject, httpServletResponse);

    }
}
