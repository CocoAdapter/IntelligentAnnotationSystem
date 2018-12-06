package sjtu.yhapter.ias.presenter;

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
import io.reactivex.schedulers.Schedulers;
import sjtu.yhapter.ias.App;
import sjtu.yhapter.ias.model.pojo.Book;
import sjtu.yhapter.ias.model.pojo.TeachClass;
import sjtu.yhapter.ias.presenter.contract.BookShelfContract;
import sjtu.yhapter.ias.ui.base.RxPresenter;
import sjtu.yhapter.ias.ui.dialog.BookShelfMenu;
import sjtu.yhapter.reader.util.LogUtil;

public class BookShelfPresenter extends RxPresenter<BookShelfContract.View> implements BookShelfContract.Presenter {
    @Override
    public void requestBooks(long menuIndex) {
        if (menuIndex == BookShelfMenu.ID_RECENTLY_READ) {
            Single.create((SingleOnSubscribe<List<Book>>) emitter -> {
                // TODO 从数据库取
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                emitter.onSuccess(Collections.emptyList());
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
                            view.onRequestBooks(Collections.emptyList());
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.log(e.toString());
                            view.showError();
                        }
                    });
        } else if (menuIndex == BookShelfMenu.ID_COLLECTION) {
            // TODO 从数据库取
            Single.create((SingleOnSubscribe<List<Book>>) emitter -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                emitter.onSuccess(Collections.emptyList());
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
                            view.showError();
                        }
                    });
        } else {
            // TODO 根据教学班返回书单
            Single.create((SingleOnSubscribe<List<Book>>) emitter -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {}

                List<Book> books = new ArrayList<>();
                for (int i = 0; i < 2; i ++) {
                    Book book = new Book();
                    book.setTitle("了不起的盖茨比 " + i);
                    book.setAuthor("弗·司各特·菲茨杰拉德");
                    Date nowTime = new Date();
                    book.setCreatedTime(nowTime);
                    book.setUpdatedTime(nowTime);
                    book.setStudentId(1L);
                    book.setTeachCourseId((long) (i % 2));

                    books.add(book);
                }

                App.getDaoInstant().getBookDao().insertOrReplaceInTx();
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
                            view.showError();
                        }
                    });
        }
    }

    @Override
    public void requestMenu() {
        Single.create((SingleOnSubscribe<List<TeachClass>>) emitter -> {
            List<TeachClass> teachClasses = App.getDaoInstant().getTeachClassDao().loadAll();
            LogUtil.log("presenter : " + teachClasses.toString());
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
                        LogUtil.log(teachClasses.toString());
                        view.onRequestMenu(teachClasses);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.log(e.toString());
                        view.showError();
                    }
                });
    }
}
