package com.pingcap.tikv.kv;

import java.util.Iterator;

public interface Retriever {
  // Get gets the value for key k from kv store.
  // If corresponding kv pair does not exist, it returns nil and ErrNotExist.
  byte[] get(byte[] key);

  // Seek creates an Iterator positioned on the first entry that k <= entry's key.
  // If such entry is not found, it returns an invalid Iterator with no error.
  // The Iterator must be Closed after use.
  Iterator<byte[]> seek(byte[] key);

  // SeekReverse creates a reversed Iterator positioned on the first entry which key is less than k.
  // The returned iterator will iterate from greater key to smaller key.
  // If k is nil, the returned iterator will be positioned at the last key.
  Iterator<byte[]> seekReverse(byte[] key);
}
