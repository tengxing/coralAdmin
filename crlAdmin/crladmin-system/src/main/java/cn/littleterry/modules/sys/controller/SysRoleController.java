package cn.littleterry.modules.sys.controller;


import cn.littleterry.aop.log.Log;
import cn.littleterry.modules.sys.entity.SysRole;
import cn.littleterry.modules.sys.entity.SysRoleMenu;
import cn.littleterry.modules.sys.entity.SysRolePermission;
import cn.littleterry.modules.sys.entity.dto.SysRoleDTO;
import cn.littleterry.modules.sys.service.*;
import cn.littleterry.util.R;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 角色表
 *
 * @author terry
 * @since 2019-05-03
 */
@RestController
@RequestMapping("sys/role")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysRolePermissionService sysRolePermissionService;

    /**
     * 列表
     */
    @ApiOperation("分页查询角色列表信息")
    @RequestMapping("/list")
    public R list(@RequestParam(name="page", defaultValue="1") Integer pageNo,
                  @RequestParam(name="size", defaultValue="10") Integer pageSize){

        Map<String,Object> map = new HashMap<>();
        map.put("page",pageNo);
        map.put("size",pageSize);
        IPage<SysRole> pageList = sysRoleService.listByPage(map);

        /**
         * 获取角色菜单以操作权限
         */
        List<SysRoleDTO> sysRoleDTOList = new ArrayList();
        for (SysRole item : pageList.getRecords()){
            SysRoleDTO sysRoleDTO = new SysRoleDTO();
            BeanUtils.copyProperties(item,sysRoleDTO);
            sysRoleDTO.setMenus(sysMenuService.findByRoleId(sysRoleDTO.getId()));
            sysRoleDTO.setPermissions(sysPermissionService.findByRoleId(sysRoleDTO.getId()));
            sysRoleDTOList.add(sysRoleDTO);
        }

        return R.ok().write(sysRoleDTOList);
    }

    /**
     * 更新角色菜单
     */
    @ApiOperation("更新角色菜单")
    @RequestMapping("/updateMenu")
    public R updateMenu(@RequestBody SysRoleDTO roleDTO){
        List<SysRoleMenu> roleMenus = sysRoleMenuService.listByRoleId(roleDTO.getId());

        List<Long> oldIds = roleMenus.stream().map(item ->item.getId()).collect(Collectors.toList());
        sysRoleMenuService.removeByIds(oldIds);

        List<SysRoleMenu> newRoleMenus = new ArrayList<>();
        roleDTO.getMenus().forEach(item ->{
            SysRoleMenu tmp = new SysRoleMenu();
            tmp.setRoleId(roleDTO.getId());
            tmp.setMenuId(item.getId());
            newRoleMenus.add(tmp);
        });
        sysRoleMenuService.saveBatch(newRoleMenus);
        return R.ok();
    }

    /**
     * 更新角色权限
     */
    @ApiOperation("更新角色权限")
    @RequestMapping("/updatePermission")
    public R updatePermission(@RequestBody SysRoleDTO roleDTO){
        List<SysRolePermission> rolePermissions = sysRolePermissionService.listByRoleId(roleDTO.getId());

        List<Long> oldIds = rolePermissions.stream().map(item ->item.getId()).collect(Collectors.toList());
        sysRolePermissionService.removeByIds(oldIds);

        List<SysRolePermission> newRolePermissions = new ArrayList<>();
        roleDTO.getPermissions().forEach(item ->{
            SysRolePermission tmp = new SysRolePermission();
            tmp.setRoleId(roleDTO.getId());
            tmp.setPermissionId(item.getId());
            newRolePermissions.add(tmp);
        });
        sysRolePermissionService.saveBatch(newRolePermissions);
        return R.ok();
    }

    /**
     * 获取所有角色信息
     * @return
     */
    @ApiOperation("获取所有角色信息")
    @RequestMapping("/all")
    public R<List<SysRole>> all(){
        return R.ok().write(sysRoleService.list());
    }


    /**
     * 信息
     */
    @ApiOperation("查询角色信息")
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		SysRole sysRole = sysRoleService.getById(id);
        SysRoleDTO sysRoleDTO = new SysRoleDTO();
        BeanUtils.copyProperties(sysRole,sysRoleDTO);
        sysRoleDTO.setMenus(sysMenuService.findByRoleId(sysRoleDTO.getId()));
        sysRoleDTO.setPermissions(sysPermissionService.findByRoleId(sysRoleDTO.getId()));

        return R.ok().write(sysRoleDTO);
    }

    /**
     * 保存
     */
    @ApiOperation("新增角色信息")
    @RequestMapping("/add")
    public R add(@RequestBody SysRole sysRole){
		sysRoleService.save(sysRole);

        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation("修改角色信息")
    @RequestMapping("/modify")
    public R modify(@RequestBody SysRole sysRole){
		sysRoleService.updateById(sysRole);

        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation("删除角色信息")
    @RequestMapping("/remove/{id}")
    public R remove(@PathVariable("id") Long id){
        sysRoleService.removeById(id);

        return R.ok();
    }

}
