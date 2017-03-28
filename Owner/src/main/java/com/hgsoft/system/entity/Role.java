package com.hgsoft.system.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Module entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class Role  implements java.io.Serializable {


    // Fields    

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String remark;
    private Set modules = new HashSet(0);


    // Constructors

    /** default constructor */
    public Role() {
    }

    
    /** full constructor */
    public Role(String name, String remark) {
        this.name = name;
        this.remark = remark;
    }

    @Override
    public String toString() {
    	return "role>>>[id:"+this.id+",name:"+this.name+"]";
    }
   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return this.remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }

	public Set getModules() {
		return modules;
	}

	public void setModules(Set modules) {
		this.modules = modules;
	}
}