- [TODO] 微信读书书页内部可以嵌入按钮，这种考虑写成另一种Page
- [TODO] 每次都重绘了两层bitmap，考虑重写PageAnimation和ReaderView之间的关系
- [OPT] 考虑让LineElement和TextSelector组合, readerView不参与数据交互
- [OPT] 分页不支持并发，章节数据只能排队分页，考虑优化
- [BUG] 肯定哪里存在内存泄漏，内存从60MB -> 增到100左右
- [OPT] 预加载实在是有问题, 太难受了, 写不好
用countDownLatch来实现预加载队列的实现？ Single.zip 好像能解决！
- [BUG] reader里的disposable 好像没有释放???