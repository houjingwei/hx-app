公共标题栏用法
Activity:

@ViewInject(R.id.myTitle)
CommonTitle commonTitle;

x.view().inject(this);
commonTitle.setActivity(this);
commonTitle.setTitleText(getResources().getString(R.string.moneyBag));

