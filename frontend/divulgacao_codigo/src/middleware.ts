import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

const publicRoutes = ['/login','/register'];

export function middleware(request: NextRequest){
    const cookie = request.cookies.get('accessToken')?.value;
    const {pathname} = request.nextUrl;

    const isProtectedRoute = !publicRoutes.includes(pathname)

    if(!cookie && isProtectedRoute){
        const loginUrl = new URL('/login',request.url)
        loginUrl.searchParams.set('redirect_to',pathname)

        return NextResponse.redirect(loginUrl)
    }

    if(cookie && publicRoutes.includes(pathname) && pathname!=='/register'){
        return NextResponse.redirect(new URL('/home',request.url))
    }

    return NextResponse.next();
}

export const config = {
    matcher:[
        '/((?!api|_next/static|_next/image|favicon.ico).*)'
    ]
};
