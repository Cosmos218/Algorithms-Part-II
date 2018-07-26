# Tips for SeamCarving

1.要让构造方法和return方法中Picture为immutable，需用copy的constructor复制：

```java
    public SeamCarver(Picture picture) {               // create a seam carver object based on the given picture
        if (picture == null)
            throw new IllegalArgumentException("picture is null!");
        this.picture = new Picture(picture);    // mutate
    }

    public Picture picture() {                         // current picture
        return new Picture(picture);     // mutate
    }
```

这在软件工程中十分重要，减少耦合性，传新对象而不是传引用。

2.i和j的顺序很头疼，多数bug都是因为这个。还有remove时的小操作也比较复杂。

3.如何处理内存开销：将全局变量distTo, edgeTo转化为局部变量。同时不将energy提前存入全局变量，而是每次调用的时候算出来一个。时间复杂度并没有增长，而且速度快了。transpose则直接操作picture，不影响速度。

4.核心思想：找vertical seam时，一排一排从上至下进行relax操作的顺序就是拓扑顺序。relax即根据上面三个（或2个或1个）点更新distTo数组和edgeTo数组。

思想很简单，但大部分时间花在了处理数组横纵坐标以及debug上。
