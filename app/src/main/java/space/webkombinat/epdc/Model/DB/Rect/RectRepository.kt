package space.webkombinat.epdc.Model.DB.Rect

class RectRepository(
    private val rectDao: RectDao
) {
    suspend fun insertRect(rect: RectEntity): Long {
        rectDao.create(rect)
        return rect.id
    }
}