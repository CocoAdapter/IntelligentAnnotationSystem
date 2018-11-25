## 1. [OPT] Bitmap 需要做缓存
## 2. [TODO] 微信读书书页内部可以嵌入按钮，这种考虑写成另一种Page
## 3. [OPT] 字符的点数据每次draw都会重新计算，需要缓存+判断
## 4. [TODO] 每次都重绘了两层bitmap，考虑重写PageAnimation和ReaderView之间的关系
## 5. [OPT] 考虑让LineElement和TextSelector组合, readerView不参与数据交互