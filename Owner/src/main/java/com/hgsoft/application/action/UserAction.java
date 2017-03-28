package com.hgsoft.application.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.hgsoft.application.util.Base64;
import com.hgsoft.application.util.RandomSecretUtil;
import com.hgsoft.application.util.SecretUtils;
import com.hgsoft.application.util.SessionUtil;
import com.hgsoft.application.vo.ApplicationVo;
import com.hgsoft.application.vo.Status;
import com.hgsoft.carowner.entity.MebUser;
import com.hgsoft.carowner.service.MebUserService;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.MD5Coder;

@Controller
@Scope("prototype")
public class UserAction extends ApplicationBaseAction {

	@Resource
	private MebUserService userService;
	private String password;
	private MebUser user;
	private String obdSn;
	private String carId;
	private String license;
	private String name;
	private String sex;
	private String payPass;
	private String checkNum;
	private String random;

	public void login() {
		if (StringUtils.isEmpty(getUsername()) || StringUtils.isEmpty(password)) {
			outJsonMessage(new ApplicationVo(Status.NOINFO, "错误登陆").getJson());
		} else {
			user = userService.queryUserByMobile(getUsername().trim());
			if (user != null && user.getRegUserId() != null) {
				try {
					String key = RandomSecretUtil.getType(getType()) + "#" + getRandom();
					password = password.replace("$", "=");
					byte[] bt = Base64.decode(password);
					password = new String(SecretUtils.decryptMode(bt, key));
					System.out.println(password);
					password = MD5Coder.encodeMD5Hex(password);
					System.out.println(password);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String sessionId = getRequest().getSession().getId();
				SessionUtil.reLogin(getUsername(), sessionId);
				if (password.equals(user.getPassword())) {
					getRequest().getSession().setAttribute("username", getUsername());
					user.setPassword("");
					user.setPayPassword("");
					ApplicationVo vo = new ApplicationVo(Status.SUCCESS, "登陆成功");
					vo.put("userInfo", user);
					outJsonMessage(vo.getJson());
				} else {
					outJsonMessage(new ApplicationVo(Status.FAIL, "密码错误").getJson());
				}
			} else {
				outJsonMessage(new ApplicationVo(Status.FAIL, "用户不存在").getJson());
			}
		}
	}

	public void checkUserName() {
		if (StringUtils.isEmpty(getUsername())) {
			outJsonMessage(new ApplicationVo(Status.NOINFO, "非法登录").getJson());
		} else {
			user = userService.queryUserByMobile(getUsername());
			if (user == null) {
				String checkNum = RandomSecretUtil.randomString(8);
				getRequest().getSession().setAttribute("checkNum", checkNum);
				outJsonMessage(new ApplicationVo(Status.SUCCESS, checkNum).getJson());
				getRequest().getSession().setAttribute("username", getUsername());
			} else {
				outJsonMessage(new ApplicationVo(Status.HASUSER, "手机号已注册").getJson());
			}
		}
	}

	public void register() {
		if (StringUtils.isEmpty(getUsername()) || StringUtils.isEmpty(password) || StringUtils.isEmpty(checkNum)) {
			outJsonMessage(new ApplicationVo(Status.NOINFO, "信息不完整").getJson());
		} else {
			user = userService.queryUserByMobile(getUsername());
			if (user == null) {
				if (!checkNum.equals(getRequest().getSession().getAttribute("checkNum"))) {
					outJsonMessage(new ApplicationVo(Status.FAIL, "错误请求").getJson());
				} else {
					try {
						String key = RandomSecretUtil.getType(getType()) + "#" + getRandom();
						password = password.replace("$", "=");
						byte[] bt = Base64.decode(password);
						password = new String(SecretUtils.decryptMode(bt, key));
						System.out.println(password);
						password = MD5Coder.encodeMD5Hex(password);
						System.out.println(password);
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException();
					}
					user = new MebUser();
					user.setPassword(password);
					user.setMobileNumber(getUsername());
					user.setUserType(1);
					user.setValid("1");
					String uid = IDUtil.createID();
					user.setRegUserId(uid);
					userService.save(user);
					getRequest().getSession().removeAttribute("checkNum");
					//String message = "注册成功";
					/*outJsonMessage(new ApplicationVo(Status.SUCCESS, message).getJson());*/
					StringBuffer bf = new StringBuffer("{\"status\":\"SUCCESS\",\"message\":\"注册成功\",\"uid\":\"");
					bf.append(uid);
					bf.append("\"}");
					outJsonMessage(bf.toString());
				}
			} else {
				outJsonMessage(new ApplicationVo(Status.FAIL, "手机号已注册，请登录").getJson());
			}
		}
	}

	public void getUserInfo() {
		if (StringUtils.isEmpty(getUsername())) {
			outJsonMessage(new ApplicationVo(Status.FAIL, "没有用户信息").getJson());
		} else {
			user = userService.queryByMobileNumber(getUsername());
			if (user != null) {
				user.setPassword("");
				user.setPayPassword("");
				ApplicationVo vo = new ApplicationVo(Status.SUCCESS, "信息获取成功");
				vo.put("userInfo", user);
				outJsonMessage(vo.getJson());
			} else {
				outJsonMessage(new ApplicationVo(Status.FAIL, "没有找到该账户").getJson());
			}
		}
	}

	public void updateUserInfo() {
		if (StringUtils.isEmpty(getUsername())) {
			outJsonMessage(new ApplicationVo(Status.FAIL, "没有用户信息").getJson());
		} else {
			user = userService.queryByMobileNumber(getUsername());
			if (user != null) {
				if (StringUtils.isEmpty(obdSn)) {
					user.setObdSN(obdSn);
				}
				if (StringUtils.isEmpty(carId)) {
					user.setCarId(carId);
				}
				if (StringUtils.isEmpty(license)) {
					user.setLicense(license);
				}
				if (StringUtils.isEmpty(name)) {
					user.setName(name);
				}
				if (StringUtils.isEmpty(sex)) {
					user.setSex(sex);
				}
				userService.update(user);
				user.setPassword("");
				user.setPayPassword("");
				ApplicationVo vo = new ApplicationVo(Status.SUCCESS, "修改成功");
				vo.put("userInfo", user);
				outJsonMessage(vo.getJson());
			} else {
				outJsonMessage(new ApplicationVo(Status.FAIL, "没有找到该账户").getJson());
			}
		}
	}

	public void logout() {
		if (StringUtils.isEmpty(getUsername())) {
			outJsonMessage(new ApplicationVo(Status.FAIL, "没有用户信息").getJson());
		} else {
			user = userService.queryByMobileNumber(getUsername());
			if (user != null) {
				getRequest().getSession().removeAttribute("user");
				outJsonMessage(new ApplicationVo(Status.SUCCESS, "退出成功").getJson());
			} else {
				outJsonMessage(new ApplicationVo(Status.FAIL, "没有找到该账户").getJson());
			}
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getObdSn() {
		return obdSn;
	}

	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPayPass() {
		return payPass;
	}

	public void setPayPass(String payPass) {
		this.payPass = payPass;
	}

	public String getCheckNum() {
		return checkNum;
	}

	public void setCheckNum(String checkNum) {
		this.checkNum = checkNum;
	}

	public String getRandom() {
		return random;
	}

	public void setRandom(String random) {
		this.random = random;
	}

}
