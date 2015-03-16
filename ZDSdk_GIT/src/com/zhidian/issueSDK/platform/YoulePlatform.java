package com.zhidian.issueSDK.platform;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;

import com.zhidian.gamesdk.listener.ILogOutListener;
import com.zhidian.gamesdk.listener.ILoginListener;
import com.zhidian.gamesdk.listener.InitListener;
import com.zhidian.gamesdk.manager.ZhiDianManager;
import com.zhidian.issueSDK.model.GameInfo;
import com.zhidian.issueSDK.model.UserInfoModel;
import com.zhidian.issueSDK.service.CreateRoleService.CreateRoleListener;
import com.zhidian.issueSDK.service.ExitService.GameExitListener;
import com.zhidian.issueSDK.service.InitService.GameInitListener;
import com.zhidian.issueSDK.service.LogOutService.GameLogoutListener;
import com.zhidian.issueSDK.service.LoginService.GameLoginListener;
import com.zhidian.issueSDK.service.OrderGenerateService.OrderGenerateListener;
import com.zhidian.issueSDK.service.SetGameInfoService.SetGameInfoListener;
import com.zhidian.issueSDK.util.SDKLog;
import com.zhidian.issueSDK.util.SDKUtils;

/**
 * @Description
 * @author ZengQBo
 * @time 2014年12月22日
 */
public class YoulePlatform implements Iplatform {

	private static final String TAG = "YoulePlatform";

	private GameInitListener gameInitListener;

	private GameLoginListener gameLoginListener;
	
	private static UserInfoModel model = new UserInfoModel();

	/**
	 * 初始化监听器
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void initSuccess() {
			gameInitListener.initSuccess(false, null);
		}

		@Override
		public void initFail(String message) {
			if (message == "-2") {
				gameInitListener.initFail("请检查配置文件参数");
			} else if (message == "-1") {
				gameInitListener.initFail("没有SD卡不能使用SDK");
			} else if (message == "-3") {
				gameInitListener.initFail("下载失败html。。。");
			} else {
				gameInitListener.initFail(message);
			}
		}
	};



	/**
	 * 登录监听器
	 */
	private ILoginListener loginListener = new ILoginListener() {

		@Override
		public void loginging() {

		}

		@Override
		public void loginSuccess(String sessionId, String uid) {
			model.sessionId = sessionId;
			model.id = uid;
			gameLoginListener.LoginSuccess(model);
		}

		@Override
		public void loginFail() {
			gameLoginListener.LoginFail("");
		}
	};

	private GameLogoutListener gameLogoutListener;

	private ILogOutListener iLogOutListener = new ILogOutListener() {

		@Override
		public void logouting() {

		}

		@Override
		public void logoutSuccess() {
			gameLogoutListener.logoutSuccess();
		}

		@Override
		public void logoutFail() {
		}
	};

	@Override
	public String getPlatformId() {
		return "1009";
	}

	@Override
	public void init(Activity activity, GameInitListener gameInitListener,
			GameLoginListener gameLoginListener) {
		this.gameInitListener = gameInitListener;
		this.gameLoginListener = gameLoginListener;
		ZhiDianManager.init(activity, Integer.parseInt(SDKUtils.getMeteData(
				activity, "screenOrientation")),
				mInitListener, loginListener);
	}

	@Override
	public void login(Activity activity, GameLoginListener gameLoginListener) {
		this.gameLoginListener = gameLoginListener;
		SDKLog.e(TAG, "login  model.id =====  " + model.id);
		if (TextUtils.isEmpty(model.id)) {
			ZhiDianManager.showLogin(activity, loginListener);
		}
	}

	@Override
	public void logOut(final Activity activity,
			GameLogoutListener gameLogoutListener) {
		this.gameLogoutListener = gameLogoutListener;
		cleanRes();
		ZhiDianManager.logout(activity, iLogOutListener);
	}

	private void cleanRes() {
		model = null;
	}

	@Override
	public void pay(Activity activity, String money, String order,
			GameInfo gameInfo, String notifyUrl, String exInfo,
			OrderGenerateListener listener) {
		if (money != null || Integer.parseInt(money) > 0) {
			ZhiDianManager.customPay(activity, Integer.parseInt(money), order);
		} else {
			ZhiDianManager.payNormal(activity, order);
		}

	}

	@Override
	public boolean suportLogoutUI() {
		return false;
	}

	@Override
	public void onDestory() {

	}

	@Override
	public void showFloat(Activity activity) {
		ZhiDianManager.showFloadButton(activity);
	}

	@Override
	public void setGameInfo(Activity activity, GameInfo gameInfo,
			SetGameInfoListener listener) {
		ZhiDianManager.setGame(activity, gameInfo.getZoneId(), gameInfo.getRoleId(), gameInfo.getRoleName());
		listener.onSuccess();
	}

	@Override
	public void createRole(Activity activity,GameInfo gameInfo, CreateRoleListener listener) {
		ZhiDianManager.creatRole(activity, gameInfo.getZoneId(), gameInfo.getRoleId(), gameInfo.getRoleName());
		listener.onSuccess();
	}

	@Override
	public void exit(Activity mActivity, GameExitListener listener) {
		cleanRes();
		listener.onSuccess();
	}

	@Override
	public void onPause(Activity activity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume(Activity activity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop(Activity activity) {
		// TODO Auto-generated method stub
		
	}

}