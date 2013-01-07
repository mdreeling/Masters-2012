require 'spec_helper'

describe User do
  it { should respond_to(:feed) }
  it { should respond_to(:relationships) }
end
