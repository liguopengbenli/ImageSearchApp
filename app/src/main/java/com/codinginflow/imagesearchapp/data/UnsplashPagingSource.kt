package com.codinginflow.imagesearchapp.data

import android.util.Log
import androidx.paging.PagingSource
import com.codinginflow.imagesearchapp.api.UnsplashApi
import retrofit2.HttpException
import java.io.IOException

private const val UNSPLASH_STRATING_PAGE_INDEX = 1

class UnsplashPagingSource(
    private val unsplashApi: UnsplashApi,
    private val query: String
) : PagingSource<Int, UnsplashPhoto>() {
    companion object{
        private const val TAG = "UnsplashPagingSource"
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val position = params.key ?: UNSPLASH_STRATING_PAGE_INDEX
        return try {
            val response = unsplashApi.searchPhotos(query, position, params.loadSize)
            val photos = response.results
            Log.d(TAG, "lig load............")
            LoadResult.Page(
                data = photos,
                prevKey = if(position == UNSPLASH_STRATING_PAGE_INDEX) null else position-1,
                nextKey = if(photos.isEmpty()) null else position+1
            )
        }catch (exception: IOException){
            Log.d(TAG, "lig error IO............")
            LoadResult.Error(exception)
        }catch (exception: HttpException){
            Log.d(TAG, "lig error HTTP............ $exception")
            LoadResult.Error(exception)
        }

    }

}