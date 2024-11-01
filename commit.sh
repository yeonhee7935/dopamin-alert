#!/bin/bash

commit_type="$1"
commit_message="$2"

if [ -z "$commit_type" ] || [ -z "$commit_message" ]; then
  echo "Usage: $0 <commit_message>"
  exit 1
fi

# Commit type에 따른 이모지와 접두사 매핑
case "$commit_type" in
  "-r")
    emoji=":recycle:"
    ;;
  "-f")
    emoji=":sparkles:"
    ;;
  "-b")
    emoji=":bug:"
    ;;
  "-m")
    emoji=":twisted_rightwards_arrows:"
    ;;
  *)
    echo "Invalid commit type. Supported types: -r(refactor), -f(feat), -b(bug), -m(merge)"
    exit 1
    ;;
esac

git add .

# 커밋 수행
final_commit_message="$emoji $commit_message"
git commit -m "$final_commit_message"
