package it.fdev.unisaconnect;

import it.fdev.scraper.RssScraper;
import it.fdev.utils.CardsAdapter;
import it.fdev.utils.CardsAdapter.CardItem;
import it.fdev.utils.MyListFragment;
import it.fdev.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentNews extends MyListFragment {

	private final String NEWS_URL = "http://www.unisa.it/modules/news/front/rss/index/idCategory/0/idStructure/1";
	private final int MAX_NEWS_NUMBER = 30;
	private final int MAX_TEXT_LENGTH = 150;
	
	private CardsAdapter adapter;
	private boolean alreadyStarted = false;
	private RssScraper rssScraper;
	private ArrayList<CardItem> cardsList = null;
	private TextView listEmptyView;
	private ListView listCardsView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new CardsAdapter(activity, R.layout.card, new ArrayList<CardItem>());
		setListAdapter(adapter);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_cards_ui, container, false);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		listEmptyView = (TextView) view.findViewById(R.id.card_list_empty);
		listCardsView = (ListView) view.findViewById(android.R.id.list);
		
		/* Metto animazione */
		LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.list_layout_controller);
		/* Indico che la listView di questo ListFragment deve avere il mio controller per l'animazione */
		getListView().setLayoutAnimation(controller);
		
		getNews(false);
	}
	
	@Override
	public void onStop() {
		if (rssScraper != null && rssScraper.isRunning) {
			rssScraper.cancel(true);
		}
		super.onStop();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String url = cardsList.get(position).getLink();
		Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(webIntent);
	}
	
	@Override
	public Set<Integer> getActionsToShow() {
		Set<Integer> actionsToShow = new HashSet<Integer>();
		actionsToShow.add(R.id.action_refresh_button);
		if (!alreadyStarted) {
			actionsToShow.add(R.id.action_loading_animation);
		}
		return actionsToShow;
	}
	
	@Override
	public void actionRefresh() {
		getNews(true);
	}

	public void getNews(boolean force) {
		if (!isAdded()) {
			return;
		}
		activity.setLoadingVisible(true, true);
		
		listCardsView.setSelectionAfterHeaderView();
		if (!force && cardsList != null) {
			showCards(null);
			activity.setLoadingVisible(false, false);
			return;
		}
		if (!Utils.hasConnection(activity)) {
			Utils.goToInternetError(activity, this);
			return;
		}
		
		alreadyStarted = true;
		if (rssScraper != null && rssScraper.isRunning) {
			activity.setLoadingVisible(true);
			return;
		}
		rssScraper = new RssScraper(NEWS_URL);
		rssScraper.setMaxItems(MAX_NEWS_NUMBER);
		rssScraper.setMaxTextLength(MAX_TEXT_LENGTH);
		rssScraper.setCallerFragment(this);
		rssScraper.execute(activity);
		return;
	}
	
	public void showCards(ArrayList<CardItem> cardsList) {
		if (!isAdded()) {
			return;			
		}
		
		if (listEmptyView == null || listCardsView == null) { 			// Dai report di crash sembra succedere a volte, non ho idea del perchè
			activity.setDrawerOpen(true);							   			// Quindi mostro lo slidingmenu per apparare
			activity.setLoadingVisible(false, false);
			return;
		}
		
		if (cardsList != null) {
			this.cardsList = cardsList;
		}
		
		if (this.cardsList == null) {				// Non ho un menu da mostrare
			listEmptyView.setVisibility(View.GONE);
			listCardsView.setVisibility(View.GONE);
			activity.setLoadingVisible(false, false);
			return;
		} else if (this.cardsList.size() == 0) {
			listEmptyView.setVisibility(View.VISIBLE);
			listCardsView.setVisibility(View.GONE);
			activity.setLoadingVisible(false, false);
			return;
		}
		
		listEmptyView.setVisibility(View.GONE);
		listCardsView.setVisibility(View.VISIBLE);
		
		adapter.clear();
		adapter.addAll(cardsList);
		adapter.notifyDataSetChanged();
		
		activity.setLoadingVisible(false, false);
	}

}