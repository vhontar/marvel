package com.v7v.marvel.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingSource;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.paging.LimitOffsetPagingSource;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.v7v.marvel.data.local.entities.CharacterEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CharacterDao_Impl implements CharacterDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CharacterEntity> __insertionAdapterOfCharacterEntity;

  private final EntityDeletionOrUpdateAdapter<CharacterEntity> __updateAdapterOfCharacterEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public CharacterDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCharacterEntity = new EntityInsertionAdapter<CharacterEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `characters_table` (`id`,`name`,`thumbnailUrl`,`updatedAt`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CharacterEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getThumbnailUrl());
        statement.bindLong(4, entity.getUpdatedAt());
      }
    };
    this.__updateAdapterOfCharacterEntity = new EntityDeletionOrUpdateAdapter<CharacterEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `characters_table` SET `id` = ?,`name` = ?,`thumbnailUrl` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CharacterEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getThumbnailUrl());
        statement.bindLong(4, entity.getUpdatedAt());
        statement.bindLong(5, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM characters_table";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<CharacterEntity> characters,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCharacterEntity.insert(characters);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final CharacterEntity character,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCharacterEntity.handle(character);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public PagingSource<Integer, CharacterEntity> getAllCharactersPaged(final String query) {
    final String _sql = "SELECT * FROM characters_table WHERE (? IS NULL OR name LIKE '%' || ? || '%')";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    _argIndex = 2;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    return new LimitOffsetPagingSource<CharacterEntity>(_statement, __db, "characters_table") {
      @Override
      @NonNull
      protected List<CharacterEntity> convertRows(@NonNull final Cursor cursor) {
        final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(cursor, "id");
        final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(cursor, "name");
        final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(cursor, "thumbnailUrl");
        final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(cursor, "updatedAt");
        final List<CharacterEntity> _result = new ArrayList<CharacterEntity>(cursor.getCount());
        while (cursor.moveToNext()) {
          final CharacterEntity _item;
          final int _tmpId;
          _tmpId = cursor.getInt(_cursorIndexOfId);
          final String _tmpName;
          _tmpName = cursor.getString(_cursorIndexOfName);
          final String _tmpThumbnailUrl;
          _tmpThumbnailUrl = cursor.getString(_cursorIndexOfThumbnailUrl);
          final long _tmpUpdatedAt;
          _tmpUpdatedAt = cursor.getLong(_cursorIndexOfUpdatedAt);
          _item = new CharacterEntity(_tmpId,_tmpName,_tmpThumbnailUrl,_tmpUpdatedAt);
          _result.add(_item);
        }
        return _result;
      }
    };
  }

  @Override
  public Object getLatestUpdatedTimestamp(final Continuation<? super Long> $completion) {
    final String _sql = "SELECT MAX(updatedAt) FROM characters_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
