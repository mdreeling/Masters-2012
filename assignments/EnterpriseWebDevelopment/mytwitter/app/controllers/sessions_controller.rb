class SessionsController < ApplicationController

  def new
  end

  def create
    user = User.find_by_email(params[:session][:email].downcase)
    if user && user.authenticate(params[:session][:password])
      sign_in user      # NEW LINE
      redirect_to user      #  NEW LINE
    else
      flash.now[:error] = 'Invalid email/password combination'
      render 'new'
    end
  end
  
  def destroy
  end
end