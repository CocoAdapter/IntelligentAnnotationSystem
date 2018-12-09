package sjtu.yhapter.ias;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxBus {
    private static volatile RxBus instance;
    private final PublishSubject<Object> eventBus = PublishSubject.create();

    public static RxBus getInstance() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null)
                    instance = new RxBus();
            }
        }
        return instance;
    }

    /**
     * 发送事件(post event)
     * @param event : event object(事件的内容)
     */
    public void post(Object event){
        eventBus.onNext(event);
    }

    /**
     *
     * @param code 类型
     * @param event 载荷
     */
    public void post(int code, Object event){
        Message msg = new Message(code,event);
        eventBus.onNext(msg);
    }

    /**
     * 返回Event的管理者,进行对事件的接受
     * @return Object 类型的事件
     */
    public Observable toObservable(){
        return eventBus;
    }

    /**
     *
     * @param clazz 保证接受到制定的类型
     * @param <T> 类型
     * @return 特定类型的事件
     */
    public <T> Observable<T> toObservable(Class<T> clazz){
        // ofType起到过滤的作用,确定接受的类型
        return eventBus.ofType(clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> Observable<T> toObservable(int code, Class<T> clazz){
        return eventBus.ofType(Message.class)
                .filter(msg -> msg.code == code && clazz.isInstance(msg.event))
                .map(msg -> (T) msg.event);

    }

    class Message {
        int code;
        Object event;

        Message(int code, Object event) {
            this.code = code;
            this.event = event;
        }
    }
}
