package com.example.android.musicapplication.search

import android.accounts.NetworkErrorException
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import com.example.android.musicapplication.R
import com.example.android.musicapplication.artistdetails.ArtistActivity
import com.example.android.musicapplication.utils.SearchResultType
import com.example.android.musicapplication.utils.showMyDialog
import com.example.android.network.Injector
import com.example.android.network.models.Artist
import com.example.android.network.repository.Callback
import kotlinx.android.synthetic.main.fragment_search.*


class SearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var searchResultsAdapter: SearchResultsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_search)

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
    }

    override fun onQueryTextSubmit(text: String?): Boolean {
        getSearchResult(text)

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        getSearchResult(newText)

        return true
    }

    private fun getSearchResult(text: String?) {
        val userInput = text?.toLowerCase()
        var list: List<Artist> = listOf()

        text?.let {
            if (it.length >= 3) {
                Injector.getArtistsRepositoryImpl().getSearchResult(text,
                    object : Callback<List<Artist>> {
                        override fun onSuccess(result: List<Artist>) {
                            setViewVisibility(SearchResultType.NOT_NULL)

                            for (artist in result) {

                                if (artist.name.toLowerCase().contains(userInput.toString())) {
                                    list = result
                                }
                            }
                            initRecycler(list)
                        }

                        override fun onError(throwable: Throwable) {
                            if (throwable is NullPointerException) {
                                setViewVisibility(SearchResultType.NULL)
                            } else if (throwable is NetworkErrorException) {
                                showMyDialog(this@SearchActivity)
                            }
                        }

                    })
            } else {
                setViewVisibility(SearchResultType.NULL)
            }
        }
    }

    private fun initRecycler(searchResult: List<Artist>) {
        val context = search_result_recycler_view.context

        searchResultsAdapter = SearchResultsAdapter()
        searchResultsAdapter.update(searchResult)

        search_result_recycler_view.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))

            val listener = object : SearchResultsAdapter.Listener {
                override fun onItemClickListener(artist: Artist) {
                    val intent = Intent(this@SearchActivity, ArtistActivity::class.java).apply {
                        putExtra("ARTIST_NAME", artist.name)
                        putExtra("ARTIST_IMAGE", artist.image)
                    }

                    startActivity(intent)
                }
            }

            searchResultsAdapter.listener = listener
            adapter = searchResultsAdapter
        }
    }

    private fun setViewVisibility(searchResultType: SearchResultType) {
        when (searchResultType) {
            SearchResultType.NOT_NULL -> {
                search_result_recycler_view.visibility = View.VISIBLE
                empty_search_result_text.visibility = View.GONE
                progress_bar.visibility = View.GONE
            }

            SearchResultType.NULL -> {
                search_result_recycler_view.visibility = View.GONE
                empty_search_result_text.visibility = View.VISIBLE
                progress_bar.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_search_menu, menu)

        val menuItem = menu?.findItem(R.id.action_search)
        val searchView = menuItem?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = getString(R.string.search)

        return true
    }
}