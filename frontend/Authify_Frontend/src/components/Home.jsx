import React from 'react'
import MenuBar from './MenuBar'
import Header from './header'

const Home = () => {
  return (
    <div className='flex flex-column items-center justify-content-center min-vh-100'>
      <MenuBar/>
      <Header/>
    </div>
  )
}

export default Home
