import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

const publicRoutes = ['/publication/search/false/false','/login','/register'];

const PADRAO_REGEX_PARA_CAPTURA = new RegExp(
  `/(\\d+)`
);

export function middleware(request: NextRequest){
    const cookie = request.cookies.get('accessToken')?.value;
    const {pathname} = request.nextUrl;

    const isProtectedRoute = !publicRoutes.includes(pathname)
    const match = pathname.match(PADRAO_REGEX_PARA_CAPTURA)

    const id = match ? match[1] : null

    if(pathname.includes("details")){
        const url = request.nextUrl.clone()
        const urlFinal = `/publication/details/${id}`
        
        if(pathname.includes(urlFinal)){
            return NextResponse.next()
        }
        
        url.pathname = urlFinal
        return NextResponse.redirect(url)
    }

    if(!cookie && isProtectedRoute){
        const searchUrl = new URL('/publication/search/false/false',request.url)

        return NextResponse.redirect(searchUrl)
    }

    if(cookie && pathname==='/login'){
        return NextResponse.redirect(new URL('/home',request.url))
    }

    if(cookie && (pathname==='/publication/search/false/false' || pathname==='/publication/search/true/false') ){
        return NextResponse.redirect(new URL('/home',request.url))
    }

    return NextResponse.next();
}

export const config = {
    matcher:[
        '/((?!api|_next/static|_next/image|favicon.ico).*)'
    ]
};
