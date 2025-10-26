import { useMemo } from "react";

export const DOTS = '...'

interface UsePaginationProps{
    totalPageCount:number;
    siblingCount: number;
    currentPage: number;
}

const range = (start:number, end:number) =>{
    let length = end - start + 1;
    return Array.from({length},(_,id)=> id+start)
}

export const usePagination = ({
    totalPageCount,
    siblingCount = 1,
    currentPage
}:UsePaginationProps) => {
    const paginationRange = useMemo(() => {

    const totalPageNumbers = (siblingCount*2) + 5;

    if (totalPageCount <= 4) {
      return range(1, totalPageCount);
    }

    const leftSiblingIndex = Math.max(currentPage - siblingCount, 1);
    const rightSiblingIndex = Math.min(
      currentPage + siblingCount,
      totalPageCount
    );

    const shouldShowLeftDots = leftSiblingIndex > 2;
    const shouldShowRightDots = rightSiblingIndex < totalPageCount - 1;

    const firstPageIndex = 1;
    const lastPageIndex = totalPageCount;

    if (!shouldShowLeftDots && shouldShowRightDots) {
      let leftItemCount = 3 + 2 * siblingCount;
      let leftRange = range(1, leftItemCount);

      while(leftRange.length > totalPageCount - 2){
        leftRange.pop()
      }

      return [...leftRange, DOTS, totalPageCount];
    }

    if (shouldShowLeftDots && !shouldShowRightDots) {
      let rightItemCount = 3 + 2 * siblingCount;
      let rightRange = range(
        totalPageCount - rightItemCount + 1,
        totalPageCount
      );
      
      while(rightRange.length > totalPageCount - 2) {
        rightRange.shift();
      }

      return [firstPageIndex, DOTS, ...rightRange];
    }

    if (shouldShowLeftDots && shouldShowRightDots) {
      let middleRange = range(leftSiblingIndex, rightSiblingIndex);
      return [firstPageIndex, DOTS, ...middleRange, DOTS, lastPageIndex];
    }
  }, [totalPageCount, siblingCount, currentPage]);

  if(totalPageCount===5){
    return range(1,5)
  }

  return paginationRange;
}