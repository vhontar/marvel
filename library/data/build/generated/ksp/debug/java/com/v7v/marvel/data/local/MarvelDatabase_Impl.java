package com.v7v.marvel.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.v7v.marvel.data.local.dao.CharacterDao;
import com.v7v.marvel.data.local.dao.CharacterDao_Impl;
import com.v7v.marvel.data.local.dao.ComicDao;
import com.v7v.marvel.data.local.dao.ComicDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MarvelDatabase_Impl extends MarvelDatabase {
  private volatile CharacterDao _characterDao;

  private volatile ComicDao _comicDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `characters_table` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `thumbnailUrl` TEXT NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `comics_table` (`id` INTEGER NOT NULL, `title` TEXT NOT NULL, `thumbnailUrl` TEXT NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'a1c1f06d1c32b9f36c6d6df9f88bd1e4')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `characters_table`");
        db.execSQL("DROP TABLE IF EXISTS `comics_table`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsCharactersTable = new HashMap<String, TableInfo.Column>(4);
        _columnsCharactersTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharactersTable.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharactersTable.put("thumbnailUrl", new TableInfo.Column("thumbnailUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharactersTable.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCharactersTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCharactersTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCharactersTable = new TableInfo("characters_table", _columnsCharactersTable, _foreignKeysCharactersTable, _indicesCharactersTable);
        final TableInfo _existingCharactersTable = TableInfo.read(db, "characters_table");
        if (!_infoCharactersTable.equals(_existingCharactersTable)) {
          return new RoomOpenHelper.ValidationResult(false, "characters_table(com.v7v.marvel.data.local.entities.CharacterEntity).\n"
                  + " Expected:\n" + _infoCharactersTable + "\n"
                  + " Found:\n" + _existingCharactersTable);
        }
        final HashMap<String, TableInfo.Column> _columnsComicsTable = new HashMap<String, TableInfo.Column>(4);
        _columnsComicsTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComicsTable.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComicsTable.put("thumbnailUrl", new TableInfo.Column("thumbnailUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComicsTable.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysComicsTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesComicsTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoComicsTable = new TableInfo("comics_table", _columnsComicsTable, _foreignKeysComicsTable, _indicesComicsTable);
        final TableInfo _existingComicsTable = TableInfo.read(db, "comics_table");
        if (!_infoComicsTable.equals(_existingComicsTable)) {
          return new RoomOpenHelper.ValidationResult(false, "comics_table(com.v7v.marvel.data.local.entities.ComicEntity).\n"
                  + " Expected:\n" + _infoComicsTable + "\n"
                  + " Found:\n" + _existingComicsTable);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "a1c1f06d1c32b9f36c6d6df9f88bd1e4", "fc870e1bcb9d44bf97d2f6a0671e12c5");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "characters_table","comics_table");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `characters_table`");
      _db.execSQL("DELETE FROM `comics_table`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(CharacterDao.class, CharacterDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ComicDao.class, ComicDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public CharacterDao characterDao() {
    if (_characterDao != null) {
      return _characterDao;
    } else {
      synchronized(this) {
        if(_characterDao == null) {
          _characterDao = new CharacterDao_Impl(this);
        }
        return _characterDao;
      }
    }
  }

  @Override
  public ComicDao comicDao() {
    if (_comicDao != null) {
      return _comicDao;
    } else {
      synchronized(this) {
        if(_comicDao == null) {
          _comicDao = new ComicDao_Impl(this);
        }
        return _comicDao;
      }
    }
  }
}
