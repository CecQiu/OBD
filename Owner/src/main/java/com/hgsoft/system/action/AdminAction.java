package com.hgsoft.system.action;

import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.entity.BaseStatus;
import com.hgsoft.common.utils.MD5Coder;
import com.hgsoft.system.entity.Admin;
import com.hgsoft.system.entity.Role;
import com.hgsoft.system.service.AdminService;
import com.hgsoft.system.service.RoleService;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.opensymphony.xwork2.ActionContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.io.Writer;
import java.util.Date;
import java.util.List;

/**
 * @author liujiefeng
 * @date May 19, 2010
 * @Description 系统用户管理
 * 
 */

@Controller
@Scope("prototype")
@SuppressWarnings({ "unchecked"})
public class AdminAction extends BaseAction {
	private final Log logger = LogFactory.getLog(AdminAction.class);

	private Admin admin;
	private String firstUrl;
	private String secondUrl;

	@Resource
	private AdminService adminService;
	@Resource
	private RoleService roleService;

	private String usernameIsExists;
	private static final String PREFIX = "{";
	private static final String NEXTFIX = "}";
	public static final String GAOGUANJU_CODE = "000";
	private String username;
	private String redirectUrl;
	private String theme;
	private String validationCode;
	

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public AdminAction() {
		admin = new Admin();
	}

	/**
	 * 跳转至新增用户界面
	 */
	public String add() {
		return ADD;
	}

	/**
	 * 保存用户
	 * @return
	 */
	public String save() {

		if (adminService.usernameIsExists(admin)) {
			usernameIsExists = "登录名已存在！";
			return add();
		}

		admin.setCreateTime(new Date());
		try {
			admin.setPassword(MD5Coder.encodeMD5Hex(admin.getPassword()
                    + PREFIX + admin.getUsername() + NEXTFIX));
		} catch (Exception e) {
		}
		//System.out.println("userid:="+adminService.getMaxAdminID()+900000001);
		admin.setUserid(adminService.getMaxAdminID()+900000001);
		adminService.save(admin);
		message = "操作成功！";
		admin = new Admin();
		return list();
	}

	/**
	 * 修改用户
	 * @return
	 */
	public String update() {
		if (admin == null) {
			message = "传入参数有误！";
			return ERROR;
		}
		Admin temp = adminService.find(admin.getId());
		if (temp == null) {
			message = "管理员信息不存在";
			return list();
		}
		temp.setUsername(admin.getUsername());
		temp.setName(admin.getName());
		if (admin.getPassword() != null && admin.getPassword().length() > 0) {
			try {
				temp.setPassword(MD5Coder.encodeMD5Hex(admin.getPassword()
						+ PREFIX + temp.getUsername() + NEXTFIX));
			} catch (Exception e) {
			}
		}

		temp.setSex(admin.getSex());
		temp.setEmail(admin.getEmail());
		temp.setPhone(admin.getPhone());
		temp.setValid(admin.getValid());
		temp.setRole(admin.getRole());
		temp.setIdCard(admin.getIdCard());
		if (adminService.usernameIsExists(temp)) {
			usernameIsExists = "登录名已存在！";
			return edit();
		}

		adminService.update(temp);
		message = "操作成功！";
		admin = new Admin();
		return list();
	}

	/**
	 * 停用用户
	 * 
	 * @return
	 */
	public String delete() {
		// adminService.deleteById(admin.getId());
		if (admin == null || admin.getId() == null) {
			this.message = "待停用的用户不存在！";
			return ERROR;
		}
		adminService.updateAdmin4Disabled(admin.getId());
		return list();
	}

	/**
	 * 启用用户
	 * 
	 * @return
	 */
	public String enable() {
		// adminService.deleteById(admin.getId());
		if (admin == null || admin.getId() == null) {
			this.message = "待启用的用户不存在！";
			return ERROR;
		}
		adminService.updateAdmin4Enable(admin.getId());
		return list();
	}

	public String edit() {
		admin = adminService.find(admin.getId());
		return EDIT;
	}

	/**
	 * 跳转至更新个人用户信息界面
	 * 
	 * @return
	 */
	public String editmyself() {
		if (null == operator.getId()) {
			return "login";
		}

		admin = adminService.find(operator.getId());
		return "editmyself";
	}

	/**
	 * 更新个人用户信息
	 * 
	 * @return
	 */
	public String updatemyself() {
		if (admin == null) {
			message = "传入参数有误！";
			return ERROR;
		}

		if (null == admin.getId()) {
			message = "管理员信息不存在";
			return ERROR;
		}
		Admin temp = adminService.find(admin.getId());
		if (temp == null) {
			message = "管理员信息不存在";
			return ERROR;
		}
		temp.setUsername(admin.getUsername());
		temp.setName(admin.getName());
		if (admin.getPassword() != null && admin.getPassword().length() > 0) {
			try {
				temp.setPassword(MD5Coder.encodeMD5Hex(admin.getPassword()
						+ PREFIX + temp.getUsername() + NEXTFIX));
			} catch (Exception e) {
			}
		}

		temp.setSex(admin.getSex());
		temp.setEmail(admin.getEmail());
		temp.setPhone(admin.getPhone());
		temp.setValid(admin.getValid());
		temp.setRole(admin.getRole());
		if (adminService.usernameIsExists(temp)) {
			usernameIsExists = "登录名已存在！";
			return editmyself();
		}

		adminService.update(temp);
		message = "操作成功！";
		return "updatemyself";
	}

	/**
	 * 查询用户列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list() {
		this.list = adminService.query(pager, admin, this.operator);
		return LIST;
	}

	/**
	 * 查询用户列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String query() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String valid = request.getParameter("valid");
		String name = request.getParameter("name");

		Admin admin = new Admin();
		admin.setName(name);
		admin.setValid(valid);

		this.list = adminService.query(pager, admin, this.operator);
		return LIST;
	}

	public String login() {
		String adminRole = "";
		if (theme == null || theme == "") {
			theme = "amsterdam";
		}
		if (admin != null && admin.getUsername() != null
				&& admin.getUsername().trim().length() > 0) {
			logger.info("用户"+admin.getUsername().trim()+"尝试登陆");
			this.username = admin.getUsername().trim();
			if (admin.getPassword() != null
					&& admin.getPassword().trim().length() > 0) {
				try {
					
					//验证码验证
					HttpServletRequest request = ServletActionContext.getRequest();
					String validationCode = request.getParameter("validationCode");
			        HttpSession session = request.getSession();

			        String validation_code = (String)session.getAttribute("validation_code");
			        
			        if(StringUtils.isEmpty("validationCode") || StringUtils.isEmpty(validation_code)){
			        	ActionContext.getContext().getSession().put("theme", theme);
			    		return LOGIN;
			        }
			        
			        if(!validationCode.equalsIgnoreCase(validation_code)){
			        	//清除验证码
			        	request.removeAttribute("validationCode");
			        	session.removeAttribute("validation_code");
			        	message="验证码错误.";
			        	ActionContext.getContext().getSession().put("theme", theme);
			    		return LOGIN;
			        }

			        String password = MD5Coder.encodeMD5Hex(admin.getPassword().trim()
							+ PREFIX + admin.getUsername().trim() + NEXTFIX);
					// 设置加密后的密码
					admin.setPassword(password);
					admin = adminService.check(admin);
			        
					if (admin != null) {
						if (!"1".equals(admin.getValid())) {
							System.out.println("您已被禁止登录，请联系管理员");
							message = "您已被禁止登录，请联系管理员";
							return LOGIN;
						}
						ActionContext.getContext().getSession().put("operator", admin);
						ActionContext.getContext().getSession()
								.put("functions", adminService.getFunctions(admin));
						adminRole = "sysAdmin";
						if(getAccountRole().equals(BaseStatus.ROLE_SHOPMANAGER)) {
							theme = "dandelion";
							adminRole = "shopAdmin";
						}
						ActionContext.getContext().getSession()
						.put("adminRole", adminRole);
						ActionContext.getContext().getSession()
								.put("theme", theme);
						return SUCCESS;
					} else {
						message = "用户名或密码错误，请重新输入";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else { 
				message = "密码为空，请重新输入";
			}
		}
		ActionContext.getContext().getSession().put("theme", theme);
		return LOGIN;
	}

	public String saveTheme() {
		if (theme != null && theme != "") {
			ActionContext.getContext().getSession().put("theme", theme);
			// adminService.saveTheme(theme, operator.getId().toString());
		}
		return "saveTheme";
	}

	public String logout() {
		ActionContext.getContext().getSession().put("operator", null);
		this.redirectUrl = "../login.do";
		return "logout";
	}

	public String info() {
		admin = adminService.find(admin.getId());
		return "info";
	}

	public String updateInfo() {
		Admin temp = adminService.find(operator.getId());
		temp.setName(admin.getName());
		if (admin.getPassword().length() > 0) {
			temp.setPassword(admin.getPassword());
		}
		temp.setSex(admin.getSex());
		temp.setEmail(admin.getEmail());
		temp.setPhone(admin.getPhone());
		adminService.update(temp);
		ActionContext.getContext().getSession().put("operator", temp);
		result = Result.SUCCESS;
		return "info";
	}

	/**
	 * 跳转至连接池监控页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showDateSource() throws Exception {
		String dataSource = "dataSource";
		HttpServletRequest request = ServletActionContext.getRequest();

		String flag = request.getParameter("flag");
		WebApplicationContext applicationContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(ServletActionContext
                        .getServletContext());

		DataSource etcDataSource = (DataSource) applicationContext
				.getBean(dataSource);

		// ajax方式输出
		if (StringUtils.isNotEmpty(flag) && StringUtils.isNotBlank(flag)) {

			if (StringUtils.endsWithIgnoreCase(flag, "reload")) {
				if (etcDataSource instanceof ComboPooledDataSource) {
					ComboPooledDataSource comboPooledDataSource = (ComboPooledDataSource) etcDataSource;

					int busy = comboPooledDataSource
							.getNumBusyConnectionsAllUsers();
					int idle = comboPooledDataSource
							.getNumIdleConnectionsAllUsers();
					int unclosed = comboPooledDataSource
							.getNumUnclosedOrphanedConnectionsAllUsers();

					HttpServletResponse response = ServletActionContext
							.getResponse();
					Writer writer = response.getWriter();
					String str = "{";
					StringBuffer sb = new StringBuffer(str);
					sb.append("\"idle\":\"" + idle + "\",");
					sb.append("\"busy\":\"" + busy + "\",");
					sb.append("\"unclosed\":\"" + unclosed + "\"}");
					str = sb.toString();

					writer.write(str);
					writer.flush();

					writer.close();
				} else {
					throw new Exception("原C3P0数据源已更换");
				}

			}
			return null;
		} else {

			if (etcDataSource instanceof ComboPooledDataSource) {
				ComboPooledDataSource comboPooledDataSource = (ComboPooledDataSource) etcDataSource;
				request.setAttribute("comboPooledDataSource",
						comboPooledDataSource);

			} else {
				throw new Exception("原C3P0数据源已更换");
			}

			return "datasource";
		}

	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public void setRoles(String roles) {
		if (roles != null) {
			roles = roles.replace("，", ",").replace(" ", "");
			String[] ids = roles.split(",");

			if (ids != null && ids.length > 0) {
				for (int i = 0; i < ids.length; i++) {
					Role role = new Role();
					role.setId(new Integer(ids[i]));
					admin.setRole(role);
				}
			}
		}
	}

	public String indexPage() {
		return "manager";
	}

	public List<Role> getRoleList() {
		String hql = "from Role";
		return roleService.queryByHQL(hql);
	}

	public String getSecondUrl() {
		return secondUrl;
	}

	public String getFirstUrl() {
		return firstUrl;
	}

	public String getUsernameIsExists() {
		return usernameIsExists;
	}

	public void setUsernameIsExists(String usernameIsExists) {
		this.usernameIsExists = usernameIsExists;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getValidationCode() {
		return validationCode;
	}

	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}
	
	
}
