package sjtu.yhapter.ias.presenter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import sjtu.yhapter.ias.App;
import sjtu.yhapter.ias.RxBus;
import sjtu.yhapter.ias.model.Constants;
import sjtu.yhapter.ias.model.dao.BookDao;
import sjtu.yhapter.ias.model.pojo.Book;
import sjtu.yhapter.ias.model.pojo.DownloadTask;
import sjtu.yhapter.ias.model.pojo.TeachClass;
import sjtu.yhapter.ias.model.remote.RemoteRepository;
import sjtu.yhapter.ias.model.remote.download.DownloadListener;
import sjtu.yhapter.ias.presenter.contract.BookShelfContract;
import sjtu.yhapter.ias.service.DownloadService;
import sjtu.yhapter.ias.ui.base.RxPresenter;
import sjtu.yhapter.ias.ui.dialog.BookShelfMenu;
import sjtu.yhapter.ias.util.FileUtils;
import sjtu.yhapter.reader.util.LogUtil;
import sjtu.yhapter.reader.util.SharedPrefUtil;

public class BookShelfPresenter extends RxPresenter<BookShelfContract.View> implements BookShelfContract.Presenter {
    @Override
    public void requestBooks(long menuIndex) {
        if (menuIndex == BookShelfMenu.ID_RECENTLY_READ) {
            Single.create((SingleOnSubscribe<List<Book>>) emitter -> {
                List<Book> books = App.getDaoInstant().getBookDao().queryBuilder()
                        .orderDesc(BookDao.Properties.LastReadTime)
                        .list();
                emitter.onSuccess(books);
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<List<Book>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            addDisposable(d);
                        }

                        @Override
                        public void onSuccess(List<Book> books) {
                            view.complete();
                            view.onRequestBooks(books);
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showError(e.getMessage());
                        }
                    });
        } else if (menuIndex == BookShelfMenu.ID_COLLECTION) {
            Single.create((SingleOnSubscribe<List<Book>>) emitter -> {
                List<Book> books = App.getDaoInstant().getBookDao().queryBuilder()
                        .where(BookDao.Properties.IsFavorite.eq(true))
                        .orderDesc(BookDao.Properties.LastReadTime)
                        .list();
                emitter.onSuccess(books);
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<List<Book>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            addDisposable(d);
                        }

                        @Override
                        public void onSuccess(List<Book> books) {
                            view.complete();
                            view.onRequestBooks(books);
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.log(e.toString());
                            view.showError(e.getMessage());
                        }
                    });
        } else {
            String uid = SharedPrefUtil.getInstance().getString(Constants.UID);
            RemoteRepository
                    .getInstance()
                    .getClassBookList(menuIndex, uid)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(books -> {
                        for (Book book : books) {
                            book.setPath(FileUtils.getCachePath() + File.separator + book.getId());

                            DownloadTask task = App.getDaoInstant().getDownloadTaskDao().load(book.getId());
                            if (task == null) {
                                task = new DownloadTask();
                                task.setId(book.getId()); // 这里可能ID冲突，还是加个前缀比较好
                                task.setLink(book.getUrl());
                                task.setLocalPath(book.getPath());
                                task.setStatus(DownloadService.STATUS_WAIT);
                                task.setTaskName("download_" + book.getId() + "_" + book.getName());
                                App.getDaoInstant().getDownloadTaskDao().insertOrReplace(task);
                            }
                            book.setDownloadTask(task);

                            TeachClass teachClass = App.getDaoInstant().getTeachClassDao().load(menuIndex);
                            if (teachClass == null) {
                                // something wrong
                                LogUtil.log(menuIndex + " teachClass not found");
                            }
                            book.setTeachClass(teachClass);
                        }
                        App.getDaoInstant().getBookDao().insertOrReplaceInTx(books);
                    })
                    .subscribe(new SingleObserver<List<Book>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            addDisposable(d);
                        }

                        @Override
                        public void onSuccess(List<Book> books) {
                            view.complete();
                            view.onRequestBooks(books);
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.log(e.toString());
                            view.complete();
                            view.showError(e.getMessage());
                        }
                    });
        }
    }

    @Override
    public void requestMenu() {
        Single.create((SingleOnSubscribe<List<TeachClass>>) emitter -> {
            List<TeachClass> teachClasses = App.getDaoInstant().getTeachClassDao().loadAll();
            // TODO 如果teachClasses 为空的话，考虑联网加载？
            emitter.onSuccess(teachClasses);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<TeachClass>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(List<TeachClass> teachClasses) {
                        view.onRequestMenu(teachClasses);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.log(e.toString());
                        view.showError(e.getMessage());
                    }
                });
    }

    @Override
    public void downloadBook(int pos, Book book) {
        final DownloadTask task = book.getDownloadTask();
        DownloadListener downloadListener = new DownloadListener() {
            @Override
            public void onStart(long totalSize) {
                task.setStatus(DownloadService.STATUS_DOWNLOADING);
                task.setSize(totalSize);
                view.onDownloadStatusUpdate(pos, true);
            }

            @Override
            public void onProgress(long currSize) {
                task.setStartTime(new Date());
                task.setStatus(DownloadService.STATUS_DOWNLOADING);
                task.setProgress(currSize);
                view.onDownloadStatusUpdate(pos, true);
            }

            @Override
            public void onFail(String errorInfo) {
                view.showError(errorInfo);

                task.setStatus(DownloadService.STATUS_ERROR);
                task.setEndTime(new Date());
                // 更新task
                App.getDaoInstant().getDownloadTaskDao().insertOrReplaceInTx(task);
                view.onDownloadStatusUpdate(pos, false);
            }

            @Override
            public void onFinish() {
                task.setStatus(DownloadService.STATUS_FINISH);
                task.setEndTime(new Date());
                // 更新task
                App.getDaoInstant().getDownloadTaskDao().insertOrReplaceInTx(task);
                view.onDownloadStatusUpdate(pos, false);
            }
        };
        // 设置监听器并交给 DownloadService 去调度
        task.setDownloadListener(downloadListener);
        RxBus.getInstance().post(task);
    }
}
