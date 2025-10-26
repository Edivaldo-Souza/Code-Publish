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

    // Quantidade de números a serem exibidos na barra de paginação
    // siblingCount + primeira página + última página + página atual + 2x reticências
    const totalPageNumbers = (siblingCount*2) + 5;

    /*
      Caso 1: Se o número total de páginas for menor que os números
      que queremos exibir, não precisamos de reticências.
      Retornamos o range de [1..totalPageCount]
    */
    if (totalPageCount <= 4) {
      return range(1, totalPageCount);
    }

    // Calcula os índices dos "irmãos" à esquerda e à direita
    const leftSiblingIndex = Math.max(currentPage - siblingCount, 1);
    const rightSiblingIndex = Math.min(
      currentPage + siblingCount,
      totalPageCount
    );

    // Lógica para decidir se as reticências devem ser mostradas
    const shouldShowLeftDots = leftSiblingIndex > 2;
    const shouldShowRightDots = rightSiblingIndex < totalPageCount - 1;

    const firstPageIndex = 1;
    const lastPageIndex = totalPageCount;

    /*
      Caso 2: Não mostrar reticências à esquerda, mas mostrar à direita.
      Isso acontece quando a página atual está perto do início.
      Ex: [1, 2, 3, 4, 5, ..., 10]
    */
    if (!shouldShowLeftDots && shouldShowRightDots) {
      let leftItemCount = 3 + 2 * siblingCount;
      let leftRange = range(1, leftItemCount);

      while(leftRange.length > totalPageCount - 2){
        leftRange.pop()
      }

      return [...leftRange, DOTS, totalPageCount];
    }

    /*
      Caso 3: Mostrar reticências à esquerda, mas não à direita.
      Isso acontece quando a página atual está perto do fim.
      Ex: [1, ..., 6, 7, 8, 9, 10]
    */
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

    /*
      Caso 4: Mostrar reticências em ambos os lados.
      Acontece quando a página atual está no meio.
      Ex: [1, ..., 4, 5, 6, ..., 10]
    */
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