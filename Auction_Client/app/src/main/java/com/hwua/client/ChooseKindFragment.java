package com.hwua.client;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.hwua.client.util.DialogUtil;
import com.hwua.client.util.HttpUtil;
import org.json.JSONArray;

/**
 * 选择类别子界面
 */
public class ChooseKindFragment extends Fragment
{
	public static final int CHOOSE_ITEM = 0x1008;
	private Callbacks mCallbacks;
	private Button bnHome;
	private ListView kindList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.choose_kind, container , false);
		bnHome = (Button) rootView.findViewById(R.id.bn_home);
		kindList = (ListView) rootView.findViewById(R.id.kindList);
		// 为返回按钮的单击事件绑定事件监听器
		bnHome.setOnClickListener(new HomeListener(getActivity()));
		// 定义发送请求的URL
		String url = HttpUtil.BASE_URL + "items_kindsDatas.action";
		try
		{
			// 向指定URL发送请求，并将服务器响应包装成JSONArray对象。
			JSONArray jsonArray = new JSONArray(HttpUtil.getRequest(url));  // ①
			// 使用ListView显示所有物品准种类
			kindList.setAdapter(new KindArrayAdapter(jsonArray, getActivity()));
		}
		catch (Exception e)
		{
			DialogUtil.showDialog(getActivity(), "服务器响应异常，请稍后再试！" , false);
			e.printStackTrace();
		}
		kindList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long kindId)
			{
				Bundle bundle = new Bundle();
				bundle.putLong("kindId", kindId);
				mCallbacks.onItemSelected(CHOOSE_ITEM , bundle);
			}
		});
		return rootView;
	}

	// 当该Fragment被添加、显示到Activity时，回调该方法
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		// 如果Activity没有实现Callbacks接口，抛出异常
		if (!(activity instanceof Callbacks))
		{
			throw new IllegalStateException("ManageKindFragment所在的Activity必须实现Callbacks接口!");
		}
		// 把该Activity当成Callbacks对象
		mCallbacks = (Callbacks) activity;
	}

	// 当该Fragment从它所属的Activity中被删除时回调该方法
	@Override
	public void onDetach()
	{
		super.onDetach();
		// 将mCallbacks赋为null。
		mCallbacks = null;
	}
}