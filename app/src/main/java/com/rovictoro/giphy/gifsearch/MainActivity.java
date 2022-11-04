package com.rovictoro.giphy.gifsearch;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import com.giphy.sdk.core.network.api.GPHApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rovictoro.giphy.R;
import com.rovictoro.giphy.dagger.GifApplication;
import com.rovictoro.giphy.models.GifModel;
import com.rovictoro.giphy.dagger.NetworkMethodsImpl;
import com.rovictoro.giphy.models.GiphyClientImpl;
import com.rovictoro.giphy.utils.Utils;
import java.util.List;
import javax.inject.Inject;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;


public class MainActivity extends AppCompatActivity implements GifContract.View{

    //@Inject
    //AppModule  aap;

    @Inject
    NetworkMethodsImpl networkMethods;
    @Inject
    Application app;
    @Inject
    GPHApiClient gphApiClient;



    public static final String TAG = MainActivity.class.getSimpleName();
    private final int itemRecycleViewCacheSize = 25;

    private TextView textView;
    private SearchView mSearchView;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private GifAdapter gifAdapter;
    private SwipeRefreshLayout swipeLayout;
    private StaggeredGridLayoutManager msGLM;

    private GifContract.Presenter presenter;

    private String searchWord = "";

    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;

    int spanCount = 2;


    @Override
    public void showProgress(){
        swipeLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress(){
        swipeLayout.setRefreshing(false);
    }
    @Override
    public void showGifList(List<GifModel> gifs){
        Single.just(gifs)
                .subscribeOn(Schedulers.trampoline())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<GifModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("SingleObserver", " onSubscribe : " + d.isDisposed());
                    }

                    @Override
                    public void onSuccess(List<GifModel> value) {

                        gifAdapter.addGifs(value);
                        Log.e("SingleObserver", " onNext value : " + value.size() + " processing item on thread " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d("SingleObserver", " onError : " + e.getMessage());
                    }
                });

        Observable.fromIterable(gifs)
                //.doOnNext(c -> Log.e("fromIterable", "processing item on thread " + Thread.currentThread().getName()))
                .subscribeOn(Schedulers.trampoline())  //single().
                //.observeOn(AndroidSchedulers.mainThread())
                .buffer(4)
                .flatMap(giff -> Observable.fromIterable(giff)
                        .doOnNext((gifff) -> {
                            Log.e("fromCallable", "gif: " + gifff.getTitle() + " processing item on thread " + Thread.currentThread().getName());
                            Utils.loadGifToCashGlide(MainActivity.this, gifff, gifAdapter);
                        }) )
                .toFlowable(BackpressureStrategy.BUFFER)
                .toList()
                .doOnSuccess(i -> {swipeLayout.setRefreshing(false);
                    Log.e("doOnSuccess","Received: " + i);})
                .doOnError(er -> { swipeLayout.setRefreshing(false);
                    Log.e("doOnError","Error Captured: " + er);})
                .subscribe();

    }
    @Override
    public void showLoadingError(String errMsg){


    }

    @Override
    public void onBackPressed()
    {
        if(mSearchView.getVisibility()==View.VISIBLE) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        super.onBackPressed();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //spanCount = getResources().getInteger(R.integer.gallery_columns);

        if (mBundleRecyclerViewState != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                    if(recyclerView != null && recyclerView.getLayoutManager() != null) {
                        recyclerView.getLayoutManager().onRestoreInstanceState(mListState);
                    }
                }
            }, 50);
        }

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            msGLM.setSpanCount(spanCount*2);
            //msGLM.setOrientation(StaggeredGridLayoutManager.HORIZONTAL);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            msGLM.setSpanCount(spanCount);
        }
        recyclerView.setLayoutManager(msGLM);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((GifApplication) getApplication()).getActivityComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        textView = (TextView) findViewById(R.id.textView);
        mSearchView = (SearchView) findViewById(R.id.searchView);
        mSearchView.setVisibility(View.GONE);
        mSearchView.onActionViewCollapsed();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchWord = s;
                search();
                fab.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                fab.show();
                return true;
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setAlpha(.65f);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchView.setVisibility(View.VISIBLE);
                mSearchView.setQueryHint("Search");
                mSearchView.onActionViewExpanded();
                swipeLayout.setRefreshing(false);
                fab.hide();
            }
        });

        gifAdapter = new GifAdapter(MainActivity.this);
        recyclerView.setAdapter(gifAdapter);
        msGLM = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        msGLM.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(msGLM);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(recyclerView.getLayoutManager() != null) {
                    ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).invalidateSpanAssignments();
                }
            }
        });

        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(itemRecycleViewCacheSize);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        //recyclerView.setItemAnimator(null);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(listener);
        swipeLayout.setColorSchemeColors( // colors for progress dialog
                ContextCompat.getColor(MainActivity.this, R.color.colorPrimary),
                ContextCompat.getColor(MainActivity.this, R.color.colorAccent),
                ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_light)
        );

        presenter = new GifPresenter(MainActivity.this, new GiphyClientImpl(gphApiClient));

    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBundleRecyclerViewState = new Bundle();
        if(recyclerView != null && recyclerView.getLayoutManager() != null) {
            mListState = recyclerView.getLayoutManager().onSaveInstanceState();
            mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
        }

        Utils.hideSoftKeyboard(this);
    }

    private final SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            search();
        }
    };

    private void search() {
        String title ="Search word - " + searchWord;
        textView.setText(title);
        gifAdapter.deleteGifs();
        if(networkMethods.isNetworkAvailable()) {
            presenter.loadGifList(searchWord);
        }

        mSearchView.setVisibility(View.GONE);
        mSearchView.onActionViewCollapsed();
    }
}


//ImageView imageView = (ImageView) findViewById(R.id.imageView);
//GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
//Glide.with(this).load(R.raw.sample_gif).into(imageViewTarget);