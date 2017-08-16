package com.tianjin.MobileInspection.activity.contact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.ContactsAdapter;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.sortListview.SideBar;
import com.tianjin.MobileInspection.entity.Contact;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.ContactsModule;
import com.tianjin.MobileInspection.module.FileUploadModule;
import com.tianjin.MobileInspection.until.CharacterParser;
import com.tianjin.MobileInspection.until.CheckUntil;
import com.tianjin.MobileInspection.until.FileUtil;
import com.tianjin.MobileInspection.until.PinyinComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 通讯录
 */
public class ContactActivity extends BaseActivity implements SectionIndexer{

	public static final String STR_NAME="name";
	public static final String STR_id="id";
	public static final String STR_OFFICENAME="officeName";
	public static final String STR_PHOTO="photo";

	private ListView mlvContacts;
	private SideBar sideBar;
	private TextView dialog;
	private ContactsAdapter adapter;

	private LinearLayout titleLayout;
	private LinearLayout mlinearBack;
	private TextView mtvTitle;
	private TextView title;
	private TextView tvNofriends;
	private EditText medtSeach;
	private ImageView mivShowSeach;

	private int lastFirstVisibleItem = -1;

	private CharacterParser characterParser;
	private List<Contact> SourceDateList;

	private PinyinComparator pinyinComparator;
	private List<Contact> filterDateList;

	private ContactsModule module;


	private Runnable runnable=new Runnable() {
		@Override
		public void run() {
			filterDateList.clear();
			filterDateList.addAll(SourceDateList);
			adapter.updateListView(filterDateList);
		}
	};

	/**
	 * 按照拼音顺序显示联系人列表
	 * @param data
     */
	private void showContacts(List<Contact> data) {
		SourceDateList = filledData(data);
		//联系人列表根据拼音排序
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new ContactsAdapter(this, SourceDateList);
		mlvContacts.setAdapter(adapter);
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(int j=0;j<SourceDateList.size();j++){
					KLog.d(SourceDateList.get(j).getHeadPath());
					if(!CheckUntil.isNull(SourceDateList.get(j).getHeadPath())) {
						String name= FileUtil.getFileNameForUrl(SourceDateList.get(j).getHeadPath());
						KLog.d(name);
						File head=new File(MyApplication.CONTACT_PATH,name);
						if(!head.exists()){
							Bitmap bit = FileUploadModule.getBitmapFromUrl(SourceDateList.get(j).getHeadPath());
							if(bit!=null) {
								FileUtil.saveFile(ContactActivity.this, MyApplication.CONTACT_PATH, name, bit);
								SourceDateList.get(j).setHead(bit);
							}
						}else {
							SourceDateList.get(j).setHead(BitmapFactory.decodeFile(head.getPath()));
						}
					}
					handler.post(runnable);
				}
			}
		}).start();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		initViews();
		initEvent();
	}

	private void initEvent() {
		module=new ContactsModule(handler,this);
		module.getContacts();
		sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					mlvContacts.setSelection(position);
				}
			}
		});

		mlvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				Toast.makeText(
						getApplication(),
						((Contact) adapter.getItem(position)).getName(),
						Toast.LENGTH_SHORT).show();
				Contact con=(Contact) adapter.getItem(position);
				Intent intent=new Intent("android.intent.action.CONTACTDETAILACTIVITY");
				intent.putExtra(ContactActivity.STR_id,con.getPersonId());
				intent.putExtra(ContactActivity.STR_NAME,con.getName());
				intent.putExtra(ContactActivity.STR_OFFICENAME,con.getOfficeName());
				intent.putExtra(ContactActivity.STR_PHOTO,con.getHeadPath());
				startActivity(intent);
			}
		});

		mlvContacts.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (SourceDateList != null) {
					int section = getSectionForPosition(firstVisibleItem);
					int nextSection = getSectionForPosition(firstVisibleItem + 1);
					int nextSecPosition = getPositionForSection(+nextSection);
					if (firstVisibleItem != lastFirstVisibleItem) {
						MarginLayoutParams params = (MarginLayoutParams) titleLayout.getLayoutParams();
						params.topMargin = 0;
						titleLayout.setLayoutParams(params);
						title.setText(SourceDateList.get(getPositionForSection(section)).getSortLetters());
					}
					if (nextSecPosition == firstVisibleItem + 1) {
						View childView = view.getChildAt(0);
						if (childView != null) {
							int titleHeight = titleLayout.getHeight();
							int bottom = childView.getBottom();
							MarginLayoutParams params = (MarginLayoutParams) titleLayout.getLayoutParams();
							if (bottom < titleHeight) {
								float pushedDistance = bottom - titleHeight;
								params.topMargin = (int) pushedDistance;
								titleLayout.setLayoutParams(params);
							} else {
								if (params.topMargin != 0) {
									params.topMargin = 0;
									titleLayout.setLayoutParams(params);
								}
							}
						}
					}
					lastFirstVisibleItem = firstVisibleItem;
				}
			}
		});

		medtSeach.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				filterData(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private void initViews() {
		titleLayout = (LinearLayout) findViewById(R.id.title_layout);
		mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
		mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
		title = (TextView) this.findViewById(R.id.title_layout_catalog);
		tvNofriends = (TextView) this.findViewById(R.id.title_layout_no_friends);
		medtSeach=(EditText)findViewById(R.id.edt_seach);
		mivShowSeach=(ImageView)findViewById(R.id.iv_seach_show);
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		mlvContacts = (ListView) findViewById(R.id.country_lvcountry);

		filterDateList = new ArrayList<Contact>();
//		SourceDateList = filledData(getResources().getStringArray(R.array.date));
		mtvTitle.setText("通讯录");
		mlinearBack.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private List<Contact> filledData(List<Contact> data) {
		for (int i = 0; i < data.size(); i++) {
			String pinyin = characterParser.getSelling(data.get(i).getName());
			String sortString = pinyin.substring(0, 1).toUpperCase();
			if (sortString.matches("[A-Z]")) {
				data.get(i).setSortLetters(sortString.toUpperCase());
			} else {
				data.get(i).setSortLetters("#");
			}
		}
		return data;

	}

	/**
	 * 根据条件显示联系人
	 * @param filterStr
     */
	private void filterData(String filterStr) {
		filterDateList.clear();
		if (TextUtils.isEmpty(filterStr)) {
			titleLayout.setVisibility(View.VISIBLE);
			mivShowSeach.setVisibility(View.VISIBLE);
			filterDateList.addAll(SourceDateList) ;
			tvNofriends.setVisibility(View.GONE);
		} else {
			mivShowSeach.setVisibility(View.GONE);
			titleLayout.setVisibility(View.GONE);
			for (Contact sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
		if (filterDateList.size() == 0) {
			tvNofriends.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	public int getSectionForPosition(int position) {
		return SourceDateList.get(position).getSortLetters().charAt(0);
	}

	public int getPositionForSection(int section) {
		for (int i = 0; i < SourceDateList.size(); i++) {
			String sortStr = SourceDateList.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.linear_return_back:
				this.finish();
				break;
		}
	}

	@Override
	public void resultData(Message msg) {
		super.resultData(msg);
		switch (msg.what){
			case ContactsModule.INT_GET_CONTACTS_SUCCESS:
				if(msg.obj!=null) {
					showContacts((ArrayList<Contact>) msg.obj);
				}
				break;
			case ContactsModule.INT_GET_CONTACTS_FILEAD:
				toLoginActivity(msg);
				break;
		}
	}
}
