var gulp = require('gulp'),
    browserSync = require('browser-sync').create(),
    autoprefixer = require('gulp-autoprefixer'),
    useref = require('gulp-useref'),
    gulpIf = require('gulp-if'),
    minifycss = require('gulp-minify-css'),
    uglify = require('gulp-uglify'),
    del = require('del'),
    sass = require('gulp-sass');

// 默认任务
gulp.task('default', ['browserSync']);


// 监听任务 创建一个broswerSync任务，我们需要告知它，根目录在哪里
gulp.task('browserSync', function () {
    // 建立浏览器自动刷新服务器
    browserSync.init({
        server: {
            baseDir: "src",
            index: "/main/webapp/WEB-INF/jsp/index.html"
        }
    });

    // 保存自动刷新浏览器
    gulp.watch('src/main/webapp/WEB-INF/**', function () {
        browserSync.reload();
    });

});

/* 自动添加css兼容前缀任务*/
gulp.task('autoprefixer', function () {
    gulp.src('src/main/webapp/WEB-INF/css/**/index.css')
        .pipe(autoprefixer({
            browsers: ['last 2 versions', 'Android >= 4.0'],//last 2 versions: 主流浏览器的最新两个版本   Android for Android WebView.
            cascade: true, //是否美化属性值 默认：true 像这样：
            //-webkit-transform: rotate(45deg);
            //        transform: rotate(45deg);
            remove: true //是否去掉不必要的前缀 默认：true
        }))
        .pipe(gulp.dest('src/main/webapp/WEB-INF/css/'));
});

/*  拼接合并压缩html中的js css减少http请求和大小
 css link前后添加   <!-- build:css css/all.css -->     <!-- endbuild -->
 js script  前后添加  <!-- build:js js/all.js -->  <!-- endbuild -->*/
gulp.task('mergeMiniJsCss', function () {
    return gulp.src('target/jinlele/WEB-INF/jsp/**/*.html')
        .pipe(useref())
        // 当是css文件时候压缩
        .pipe(gulpIf('*.css', minifycss()))
        // 当是Javascript文件时候压缩
        .pipe(gulpIf('*.js', uglify()))
        .pipe(gulp.dest('target/jinlele/WEB-INF/jsp'));
});


/*压缩css*/
gulp.task('minifycss', function () {
    return gulp.src('src/main/webapp/WEB-INF/css/**/*.css')      //压缩的文件
        .pipe(minifycss())    //执行压缩
        .pipe(gulp.dest('src/main/webapp/WEB-INF/css'));  //输出文件夹
});

/*压缩js*/
gulp.task('minifyjs', function () {
    return gulp.src('src/main/webapp/WEB-INF/js/**/*.js')
        /*  .pipe(concat('main.js'))  */  //合并所有js到main.js
        /*.pipe(gulp.dest('dist/js')) */   //输出main.js到文件夹
        /*  .pipe(rename({suffix: '.min'}))*/   //rename压缩后的文件名
        .pipe(uglify())    //压缩
        .pipe(gulp.dest('src/main/webapp/WEB-INF/js'));  //输出
});

/*执行压缩前，先删除文件夹里的内容*/
gulp.task('clean', function (cb) {
    del(['target/jinlele/WEB-INF/css', 'target/jinlele/WEB-INF/js'], cb);
});


/*使用gulp-sass插件来编译Sass*/
gulp.task('sass', function () {
    return gulp.src('app/sass/**/*.scss')
        .pipe(sass()) // Using gulp-sass
        .pipe(gulp.dest('dist/css'));
});

/*有了监听  每次修改文件  Gulp都将自动为我们执行任务*/
gulp.task("watchsass", ['browserSync', 'sass'], function () {
    gulp.watch('app/sass/**/*.scss', ['sass']);
})


