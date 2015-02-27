/*
 * Copyright 2000-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.util.xmlb;

import org.jdom.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

class TextBinding extends Binding {
  private final Class<?> valueClass;

  public TextBinding(@NotNull Accessor accessor) {
    super(accessor);

    valueClass = XmlSerializerImpl.typeToClass(accessor.getGenericType());
  }

  @Nullable
  @Override
  public Object serialize(@NotNull Object o, @Nullable Object context, @NotNull SerializationFilter filter) {
    Object value = myAccessor.read(o);
    return value == null ? null : new Text(convertToString(value));
  }

  @NotNull
  static String convertToString(@NotNull Object value) {
    if (value instanceof Date) {
      return Long.toString(((Date)value).getTime());
    }
    else {
      return value.toString();
    }
  }

  @Override
  @Nullable
  public Object deserialize(Object context, @NotNull Object node) {
    String value = ((Text)node).getValue();
    XmlSerializerImpl.doSet(context, value, myAccessor, valueClass);
    return context;
  }

  @Override
  public boolean isBoundTo(Object node) {
    return node instanceof Text;
  }

  @NotNull
  @Override
  public Class getBoundNodeType() {
    return Text.class;
  }
}
