package Tests

import android.content.Context
import com.bytepace.ditsch.database.HistoryDatabase
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Created by Viktor on 23.03.2018.
 */
class Test {
    @Test
    fun canCreateDatabase() {
        val context = mock<Context> {  }
        val history = HistoryDatabase.getInstance(context)
        assertNotNull(history)
    }
}