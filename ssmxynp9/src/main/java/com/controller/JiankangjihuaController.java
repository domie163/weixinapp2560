package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.JiankangjihuaEntity;
import com.entity.view.JiankangjihuaView;

import com.service.JiankangjihuaService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;


/**
 * 健康计划
 * 后端接口
 * @author 
 * @email 
 * @date 2021-04-14 22:52:44
 */
@RestController
@RequestMapping("/jiankangjihua")
public class JiankangjihuaController {
    @Autowired
    private JiankangjihuaService jiankangjihuaService;
    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,JiankangjihuaEntity jiankangjihua, 
		HttpServletRequest request){

        EntityWrapper<JiankangjihuaEntity> ew = new EntityWrapper<JiankangjihuaEntity>();
		PageUtils page = jiankangjihuaService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiankangjihua), params), params));
        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,JiankangjihuaEntity jiankangjihua, HttpServletRequest request){
        EntityWrapper<JiankangjihuaEntity> ew = new EntityWrapper<JiankangjihuaEntity>();
		PageUtils page = jiankangjihuaService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiankangjihua), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( JiankangjihuaEntity jiankangjihua){
       	EntityWrapper<JiankangjihuaEntity> ew = new EntityWrapper<JiankangjihuaEntity>();
      	ew.allEq(MPUtil.allEQMapPre( jiankangjihua, "jiankangjihua")); 
        return R.ok().put("data", jiankangjihuaService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(JiankangjihuaEntity jiankangjihua){
        EntityWrapper< JiankangjihuaEntity> ew = new EntityWrapper< JiankangjihuaEntity>();
 		ew.allEq(MPUtil.allEQMapPre( jiankangjihua, "jiankangjihua")); 
		JiankangjihuaView jiankangjihuaView =  jiankangjihuaService.selectView(ew);
		return R.ok("查询健康计划成功").put("data", jiankangjihuaView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        JiankangjihuaEntity jiankangjihua = jiankangjihuaService.selectById(id);
		jiankangjihua.setClicknum(jiankangjihua.getClicknum()+1);
		jiankangjihuaService.updateById(jiankangjihua);
        return R.ok().put("data", jiankangjihua);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        JiankangjihuaEntity jiankangjihua = jiankangjihuaService.selectById(id);
		jiankangjihua.setClicknum(jiankangjihua.getClicknum()+1);
		jiankangjihuaService.updateById(jiankangjihua);
        return R.ok().put("data", jiankangjihua);
    }
    


    /**
     * 赞或踩
     */
    @RequestMapping("/thumbsup/{id}")
    public R thumbsup(@PathVariable("id") String id,String type){
        JiankangjihuaEntity jiankangjihua = jiankangjihuaService.selectById(id);
        if(type.equals("1")) {
        	jiankangjihua.setThumbsupnum(jiankangjihua.getThumbsupnum()+1);
        } else {
        	jiankangjihua.setCrazilynum(jiankangjihua.getCrazilynum()+1);
        }
        jiankangjihuaService.updateById(jiankangjihua);
        return R.ok();
    }

    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody JiankangjihuaEntity jiankangjihua, HttpServletRequest request){
    	jiankangjihua.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(jiankangjihua);

        jiankangjihuaService.insert(jiankangjihua);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody JiankangjihuaEntity jiankangjihua, HttpServletRequest request){
    	jiankangjihua.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(jiankangjihua);

        jiankangjihuaService.insert(jiankangjihua);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody JiankangjihuaEntity jiankangjihua, HttpServletRequest request){
        //ValidatorUtils.validateEntity(jiankangjihua);
        jiankangjihuaService.updateById(jiankangjihua);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        jiankangjihuaService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 提醒接口
     */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request, 
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {
		map.put("column", columnName);
		map.put("type", type);
		
		if(type.equals("2")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date remindStartDate = null;
			Date remindEndDate = null;
			if(map.get("remindstart")!=null) {
				Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
				c.setTime(new Date()); 
				c.add(Calendar.DAY_OF_MONTH,remindStart);
				remindStartDate = c.getTime();
				map.put("remindstart", sdf.format(remindStartDate));
			}
			if(map.get("remindend")!=null) {
				Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH,remindEnd);
				remindEndDate = c.getTime();
				map.put("remindend", sdf.format(remindEndDate));
			}
		}
		
		Wrapper<JiankangjihuaEntity> wrapper = new EntityWrapper<JiankangjihuaEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}


		int count = jiankangjihuaService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	


}
