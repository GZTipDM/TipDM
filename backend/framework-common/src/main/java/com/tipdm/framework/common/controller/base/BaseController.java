package com.tipdm.framework.common.controller.base;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.tipdm.framework.common.Constants;

public abstract class BaseController {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	protected Pageable buildPageRequest(int pageNumber) {

		return buildPageRequest(pageNumber, Constants.PAGE_SIZE);
	}

	protected Pageable buildPageRequest(int pageNumber, int pageSize) {

		return buildPageRequest(pageNumber, pageSize, Direction.DESC, "id");
	}

	/**
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param direction Direction.DESC/Direction.ASC
	 * @param property default order by id 
	 * @return
	 */
	protected Pageable buildPageRequest(int pageNumber, int pageSize,
			Direction direction, String property) {

		return new PageRequest(pageNumber - 1, pageSize, new Sort(direction,
				property));
	}
	
	/**
	 * 日期数据绑定
	 * XSS转换
	 * @param binder
	 */
	@InitBinder  
	public void initBinder(WebDataBinder binder){  
	    binder.registerCustomEditor(Date.class, new DateEditor());
	    binder.registerCustomEditor(String.class, new StringEditor());
	}
	
	/**
	 * 将Page转换为前端表格规定的数据格式
	 * @param page
	 * @return {total:xxx,rows[{}...]}
	 */
	protected Map<String,Object> convertToMap(Page<?> page){
		
		ModelMap model = new ModelMap();
		model.addAttribute("total", page.getTotalElements());
		model.addAttribute("rows", page.getContent());

		return model;
	}
}
