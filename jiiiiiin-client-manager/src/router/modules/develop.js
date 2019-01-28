import layoutHeaderAside from '@/layout/header-aside'

const meta = { requiresAuth: true }

export default {
  path: '/develop',
  name: 'develop',
  meta,
  component: layoutHeaderAside,
  children: (pre => [
    { path: 'swagger-ui', name: `${pre}swagger-ui`, component: () => import('@/pages/develop/swagger-ui'), meta: { meta, title: '接口文档' } }
  ])('develop-')
}
