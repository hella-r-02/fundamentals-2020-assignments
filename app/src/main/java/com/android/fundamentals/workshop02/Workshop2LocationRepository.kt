package com.android.fundamentals.workshop02

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.android.fundamentals.domain.location.Location
import com.android.fundamentals.domain.location.LocationRepository
import com.android.fundamentals.workshop02_03.NewLocationRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random

class Workshop2LocationRepository(
    appContext: Context
) : LocationRepository {

    private val random = Random(10)

    /** TODO 03: here you should create [Workshop2LocationsDbHelper] instance.
     * Pass [appContext] to [Workshop2LocationsDbHelper] constructor.
     */
    private val dbHelper: Workshop2LocationsDbHelper =
        Workshop2LocationsDbHelper(context = appContext)

    /** TODO 04: here you should lazy create [readableDatabase] instance from [dbHelper].
     * Call [dbHelper.readableDatabase] inside lazy block instead of TODO().
     */
    private val readableDatabase: SQLiteDatabase by lazy { dbHelper.readableDatabase }

    /** TODO 05: here you should lazy create [writableDatabase] instance from [dbHelper].
     * Call [dbHelper.writableDatabase] inside lazy block instead of TODO().
     */
    private val writableDatabase: SQLiteDatabase by lazy { dbHelper.writableDatabase }

    override suspend fun getAllLocations(): List<Location> = withContext(Dispatchers.IO) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val projection = arrayOf(
            /** TODO 06: here you should add all column names that you need to create [Location] instance.
             * All column names are located in [LocationsContract.LocationEntry].
             * Fist column for [Location.id] is already presented below : [LocationsContract.LocationEntry.COLUMN_NAME_ID].
             */
            LocationsContract.LocationEntry.COLUMN_NAME_ID,
            LocationsContract.LocationEntry.COLUMN_NAME_TITLE,
            LocationsContract.LocationEntry.COLUMN_NAME_LATITUDE,
            LocationsContract.LocationEntry.COLUMN_NAME_LONGITUDE

        )

        // How you want the results sorted in the resulting Cursor
        val sortOrder = "${LocationsContract.LocationEntry.COLUMN_NAME_TITLE} DESC"

        readableDatabase.query(
            /** TODO 07: here instead of TODO() you should add table name for query.
             * [LocationsContract.LocationEntry.TABLE_NAME] is a table name, that should be added as a first parameter.
             */
            LocationsContract.LocationEntry.TABLE_NAME,   // The table to query
            projection, // The array of columns to return (pass null to get all)
            null,   // The columns for the WHERE clause
            null,   // The values for the WHERE clause
            null,   // don't group the rows
            null,   // don't filter by row groups
            sortOrder   // The sort order
        ).use { cursor ->

            val items = mutableListOf<Location>()
            while (cursor.moveToNext()) {
                items.add(
                    Location(
                        id = cursor.getLong(cursor.getColumnIndexOrThrow(LocationsContract.LocationEntry.COLUMN_NAME_ID)),
                        title = cursor.getString(cursor.getColumnIndexOrThrow(LocationsContract.LocationEntry.COLUMN_NAME_TITLE)),
                        latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationsContract.LocationEntry.COLUMN_NAME_LATITUDE)),

                        /** TODO 08: here instead of TODO() you should get [Location.longitude]
                         * in same way as it is already done for [Location.latitude] above.
                         * [LocationsContract.LocationEntry.COLUMN_NAME_LONGITUDE] is a column name.
                         */
                        longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationsContract.LocationEntry.COLUMN_NAME_LONGITUDE))
                    )
                )
            }

            items
        }
    }

    override suspend fun addNewAndGetUpdated(): List<Location> {
        val request = generateNewLocationRequest()
        saveNewLocation(request)
        return getAllLocations()
    }

    override suspend fun deleteByIdAndGetUpdated(id: Long): List<Location> {
        deleteById(id)
        return getAllLocations()
    }

    private suspend fun generateNewLocationRequest(): NewLocationRequest =
        withContext(Dispatchers.Default) {
            delay(DELAY_MILLIS)
            NewLocationRequest(
                latitude = random.nextDouble(),
                longitude = random.nextDouble(),
                title = "Title ${random.nextInt()}"
            )
        }

    private suspend fun saveNewLocation(request: NewLocationRequest) = withContext(Dispatchers.IO) {
        writableDatabase.beginTransaction()
        try {

            val contentValues = ContentValues()
            contentValues.put(LocationsContract.LocationEntry.COLUMN_NAME_TITLE, request.title)

            /** TODO 15: here instead of TODO() you should pass [request.latitude]
             * to [contentValues] via [ContentValues.put] function.
             */
            contentValues.put(
                LocationsContract.LocationEntry.COLUMN_NAME_LATITUDE,
                request.latitude as Double
            )

            /** TODO 16: here instead of TODO() you should pass [request.longitude]
             * to [contentValues] via [ContentValues.put] function.
             */
            contentValues.put(
                LocationsContract.LocationEntry.COLUMN_NAME_LONGITUDE,
                request.longitude as Double
            )

            writableDatabase.insert(
                LocationsContract.LocationEntry.TABLE_NAME,
                null,
                contentValues
            )

            /** TODO 13: here you should mark transaction as successful.
             * call [SQLiteDatabase.setTransactionSuccessful] on [writableDatabase] instance: [writableDatabase.setTransactionSuccessful()].
             */
            writableDatabase.setTransactionSuccessful()
        }
        finally {
            writableDatabase.endTransaction()
        }
    }

    private suspend fun deleteById(id: Long) = withContext(Dispatchers.IO) {
        writableDatabase.beginTransaction()
        try {
            // Define 'where' part of query.
            val selection = "${LocationsContract.LocationEntry.COLUMN_NAME_ID} = ?"
            // Specify arguments in placeholder order.
            val selectionArgs = arrayOf(id.toString())

            writableDatabase.delete(
                LocationsContract.LocationEntry.TABLE_NAME,
                selection,
                /** TODO 17: here instead of TODO() you should add [selectionArgs].*/
                selectionArgs
            )

            writableDatabase.setTransactionSuccessful()
        } finally {
            writableDatabase.endTransaction()
        }
    }

    companion object {
        private const val DELAY_MILLIS: Long = 1_000
    }
}