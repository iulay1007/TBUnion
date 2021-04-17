package com.example.tbunion.view;

import com.example.tbunion.model.domain.Categories;
import com.example.tbunion.base.IBaseCallback;

public interface IHomeCallback extends IBaseCallback {
    void onCategoriesLoaded(Categories category);

}
