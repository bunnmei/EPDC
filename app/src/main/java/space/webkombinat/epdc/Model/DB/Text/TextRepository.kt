package space.webkombinat.epdc.Model.DB.Text

import jakarta.inject.Inject

class TextRepository @Inject constructor(
    private val textDao: TextDao
) {
    suspend fun insertText(text: TextEntity): Long {

        return textDao.create(text)
    }

    suspend fun updateText(text: TextEntity) {
        textDao.update(text)
    }

    suspend fun deleteText(text: TextEntity) {
        textDao.delete(text)
    }
}