package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.customview.CircleImg;
import com.tianjin.MobileInspection.entity.Contact;

import java.util.List;

public class ContactsChoiceAdapter extends BaseAdapter implements SectionIndexer {
	private List<Contact> list = null;
	private Context mContext;

	public ContactsChoiceAdapter(Context mContext, List<Contact> list) {
		this.mContext = mContext;
		this.list = list;
	}

	public void updateListView(List<Contact> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final Contact contact = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.listview_contacts_item, null);
			viewHolder.name = (TextView) view.findViewById(R.id.tv_seach_name);
			viewHolder.type = (TextView) view.findViewById(R.id.tv_seach_group);
			viewHolder.letter=(TextView) view.findViewById(R.id.tv_seach_Letter);
			viewHolder.head=(CircleImg)view.findViewById(R.id.ci_seach_head);
			viewHolder.choice=(ImageView)view.findViewById(R.id.iv_seach_add);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		int section = getSectionForPosition(position);

		if (position == getPositionForSection(section)) {
			viewHolder.letter.setVisibility(View.VISIBLE);
			viewHolder.letter.setText(contact.getSortLetters());
		} else {
			viewHolder.letter.setVisibility(View.GONE);
		}
		viewHolder.choice.setVisibility(View.VISIBLE);
		if(contact.isChoice()){
			viewHolder.choice.setImageResource(R.drawable.list_add_choice);
		}else{
			viewHolder.choice.setImageResource(R.drawable.list_add_normal);
		}
		viewHolder.letter.setText(contact.getSortLetters());
		viewHolder.name.setText(contact.getName());
		viewHolder.type.setText(contact.getType());
		if(contact.getHead()!=null){
			viewHolder.head.setImageBitmap(contact.getHead());
		}else {
			viewHolder.head.setImageResource(R.drawable.test_head);
		}

		return view;

	}

	final static class ViewHolder {
		TextView name;
		TextView letter;
		TextView type;
		CircleImg head;
		ImageView choice;
	}
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}