package it.fdev.utils;

import com.google.analytics.tracking.android.EasyTracker;

import it.fdev.unisaconnect.MainActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class MySimpleFragment extends Fragment implements MyFragment {
	protected MainActivity activity;
	protected Resources resources;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
		resources = getResources();
		EasyTracker.getTracker().sendView(this.getClass().toString());
	}

	@Override
	public void onResume() {
		super.onResume();
		activity.hideActions();
		setVisibleActions();
	}

	public abstract void actionRefresh();

	public boolean goBack() {
		return true;
	}

	public void actionAdd() {
	}

	public void actionEdit() {
	}

	public void actionAccept() {
	}
}