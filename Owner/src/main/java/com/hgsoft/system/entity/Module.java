package com.hgsoft.system.entity;

import java.io.Serializable;


/**
 * Module entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Module implements Serializable {

    public static class Resource{
        public static final int RESOURCE = 1;
        public static final int NOT_RESOURCE = 0;
    }

	// Fields

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Module parent;
	private String name;
	private String url;
	private String functions;
	private Integer priority;
	private Integer display;
	private Integer level;
	private String remark;
    private String icon;
    private Integer resource;
    private String resourceType;

    // Constructors

	/** default constructor */
	public Module() {
	}

	/** full constructor */
	public Module(Module parent, String name, String url, String functions,
			Integer priority, Integer display, Integer level, String subsystem, String remark, String icon) {
		this.parent = parent;
		this.name = name;
		this.url = url;
		this.functions = functions;
		this.priority = priority;
		this.display = display;
		this.level = level;
		this.remark = remark;
        this.icon = icon;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Module getParent() {
		return this.parent;
	}

	public void setParent(Module parent) {
		this.parent = parent;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFunctions() {
		return this.functions;
	}

	public void setFunctions(String functions) {
		this.functions = functions;
	}

	public Integer getPriority() {
		return this.priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getDisplay() {
		return this.display;
	}

	public void setDisplay(Integer display) {
		this.display = display;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

    public Integer getResource() {
        return resource;
    }

    public void setResource(Integer resource) {
        this.resource = resource;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}