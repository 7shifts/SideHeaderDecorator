# SideHeaderDecorator

This library offers a simple way to add sticky side headers to RecyclerViews. Without touching your list adapter, headers will scroll in with the first item in a group, stick to the top of the list, then scroll out with the last item in a group.

#### Add me to your project!

```groovy
implementation 'com.github.7shifts:SideHeaderDecorator:0.2.0'
```

[![](https://jitpack.io/v/7shifts/SideHeaderDecorator.svg)](https://jitpack.io/#7shifts/SideHeaderDecorator)

## Usage

Using SideHeaderDecorator requires only two steps. First, use the HeaderProvider interface to map items to header data. Sequential items mapping to the same header data are considered a group. For example, consider a list of name Strings where the headers' data is a name's first initial.

```kotlin
val headerProvider = object : SideHeaderDecorator.HeaderProvider<Char> {
  override fun getHeader(position: Int): Char {
    return names[position].first()
  }
}
```

Second, given your header data from HeaderProvider::getHeader, implement SideHeaderDecorator's single abstract method to create header views. Pass your HeaderProvider to SideHeaderDecorator's constructor. For example, given the first initial of a name, return an inflated header view.

```kotlin
val headerDecorator = object : SideHeaderDecorator<Char>(headerProvider) {
  override fun getHeaderView(header: Char, parent: RecyclerView): View {
    val textView = LayoutInflater.from(parent.context).inflate(R.layout.header_view, parent, false) as TextView
    textView.text = header.toString()
    return textView
  }
}
```

That's it! Apply the decorator to your RecyclerView and get sticky side headers!

```kotlin
names_recycler_view.addItemDecoration(headerDecorator)
```

<img src="https://raw.githubusercontent.com/7shifts/SideHeaderDecorator/master/readme_assets/side_header_decorator_demo.gif" width=320/>

#### Additional Notes

List Item Padding
- To make room for the header view, SideHeaderDecorator steals the left padding from your list item views.

Header Placement
- The header view is placed (at first) in the top-left corner of your list item view. The header view retains its own padding.

![Header padding and placement](https://raw.githubusercontent.com/7shifts/SideHeaderDecorator/master/readme_assets/sideheaderdecorator_padding)

## License

Copyright (c) 2019 7shifts

SideHeaderDecorator is licensed under the MIT License.
