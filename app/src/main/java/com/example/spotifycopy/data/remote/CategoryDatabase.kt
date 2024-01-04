package com.example.spotifycopy.data.remote

import com.example.spotifycopy.data.entities.Categories
import com.example.spotifycopy.data.other.Constants
import com.example.spotifycopy.presentation.models.CategoryModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CategoryDatabase {
    private val fireStore = FirebaseFirestore.getInstance()
    private val categoryCollection = fireStore.collection(Constants.CATEGORY_COLLECTION)

    suspend fun getAllCategories(): List<Categories> {
        return try {
            categoryCollection.get().await().toObjects(Categories::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}