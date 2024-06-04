package com.example.bookapi.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookapi.ApiService;
import com.example.bookapi.model.Book;
import com.example.bookapi.BookListState;
import com.example.bookapi.model.GetBooksResponse;
import com.example.bookapi.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandingPageViewModel extends ViewModel {
    private MutableLiveData<List<Book>> bookListState = new MutableLiveData<>();

    public LandingPageViewModel(String o_u, String sesskey) {

        makeGetAllBooks(o_u, sesskey);
    }
    public LiveData<List<Book>> getBookListState() {
        return bookListState;
    }

    private void makeGetAllBooks(String o_u, String oauthkey) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<GetBooksResponse> call = apiService.getAllBooks(o_u, o_u, oauthkey);
        call.enqueue(new Callback<GetBooksResponse>() {
            @Override
            public void onResponse(Call<GetBooksResponse> call, Response<GetBooksResponse> response) {
                if (response.isSuccessful()) {
                    GetBooksResponse getBooksResponse = response.body();

                    List<Book> books = getBooksResponse.allBooks.books;
                    bookListState.setValue(new BookListState.Success(books).getBooks());

                } else {
                    System.out.println("Error al obtener los libros: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GetBooksResponse> call, Throwable t) {
                System.out.println("Error al obtener los libros: " + t.getMessage());
            }
        });
    }
}
