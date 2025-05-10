package space.webkombinat.epdc.Model.DB.Rect

import jakarta.inject.Inject

class RectRepository @Inject constructor(
    private val rectDao: RectDao
) {
    suspend fun insertRect(rect: RectEntity): Long {
        rectDao.create(rect)
        return rect.id
    }

    suspend fun updateRect(rect: RectEntity) {
        rectDao.update(rect)
    }

    suspend fun deleteRect(rect: RectEntity) {
        rectDao.delete(rect)
    }
}