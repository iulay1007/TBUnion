package com.example.tbunion.view;

import com.example.tbunion.model.domain.Categories;

public interface IHomeCallback {
    void onCategoriesLoaded(Categories category);

    void onNetworkError();

    void onLoading();

    void onEmpty();
}
